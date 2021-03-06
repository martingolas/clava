//
// Created by JoaoBispo on 20/01/2017.
//

#include "ClangAstDumper.h"
#include "ClangAstDumperConstants.h"

#include "clang/AST/AST.h"

#include <iostream>
#include <sstream>

using namespace clang;

/*
 * STMTS
 */

bool ClangAstDumper::dumpStmt(const Stmt* stmtAddr) {
    if(seenStmts.count(stmtAddr) != 0) {
        return true;
    }

    log(stmtAddr);

    // A StmtDumper is created for each context,
    // no need to use id to disambiguate
    seenStmts.insert(stmtAddr);

    std::ostringstream extendedId;
    extendedId << stmtAddr << "_" << id;

    // Dump location
    dumpSourceRange(extendedId.str(), stmtAddr->getLocStart(), stmtAddr->getLocEnd());




    return false;
}

void ClangAstDumper::VisitStmt(const Stmt *Node) {
    dumpStmt(Node);

//    llvm::errs() << "DUMPING STMT: " << getId(Node) << "\n";


    for (const Stmt *SubStmt : Node->children()) {
        if (SubStmt) {
            VisitStmtTop(SubStmt);
        }
    }

}


void ClangAstDumper::VisitDeclStmt(const DeclStmt *Node) {
    if(dumpStmt(Node)) {
        return;
    }

    for (DeclStmt::const_decl_iterator I = Node->decl_begin(), E = Node->decl_end(); I != E; ++I) {
        VisitDeclTop(*I);
    }

}
void ClangAstDumper::VisitCXXForRangeStmt(const CXXForRangeStmt *Node) {
    if(dumpStmt(Node)) {
        return;
    }

    VisitStmtTop(Node->getRangeStmt());
    VisitStmtTop(Node->getBeginEndStmt());
    VisitStmtTop(Node->getCond());
    VisitStmtTop(Node->getInc());
    VisitStmtTop(Node->getBody());
}

void ClangAstDumper::VisitCompoundStmt(const CompoundStmt *Node) {
    if(dumpStmt(Node)) {
        return;
    }

    for (auto &Arg : Node->body()) {
        VisitStmtTop(Arg);
    }
}

void ClangAstDumper::VisitForStmt(const ForStmt *Node) {
    if(dumpStmt(Node)) {
        return;
    }
    if(Node->getInit() != nullptr) {
        VisitStmtTop(Node->getInit());
    }

    if(Node->getCond() != nullptr) {
        VisitStmtTop(Node->getCond());
    }

    if(Node->getInc() != nullptr) {
        VisitStmtTop(Node->getInc());
    }

    if(Node->getConditionVariable()!= nullptr) {
        VisitDeclTop(Node->getConditionVariable());
    }

    if(Node->getBody() != nullptr) {
        VisitStmtTop(Node->getBody());
    }




}

// EXPR

void ClangAstDumper::VisitCXXConstructExpr(const CXXConstructExpr *Node) {
    if(dumpStmt(Node)) {
        return;
    }

//    llvm::errs() << "DUMPING CXX CONST: " << getId(Node) << "\n";

        const Type* constructorType = Node->getConstructor()->getType().getTypePtrOrNull();
        if(constructorType != nullptr) {
            llvm::errs() << "CONSTRUCTOR_TYPE\n";
            llvm::errs() << getId(Node) << "->" << getId(constructorType) << "\n";
            //llvm::errs() << Node << "_" << id << "->" << constructorType << "_" << id << "\n";
            //VisitTypeTop(Node->getConstructor()->getType().getTypePtr());
            VisitTypeTop(constructorType);
        } else {
            llvm::errs() << "CXXConstructExpr " << Node->getConstructor()->getDeclName().getAsString() << " has null type\n";
        }
}

void ClangAstDumper::VisitUnaryExprOrTypeTraitExpr(const UnaryExprOrTypeTraitExpr *Node) {
    if(dumpStmt(Node)) {
        return;
    }

    // If argument type, dump mapping between node and type
    if (Node->isArgumentType()) {
        // Dump type
        VisitTypeTop(Node->getArgumentType().getTypePtr());

        llvm::errs() << "<UnaryExprOrTypeTraitExpr ArgType>\n";
        // First address is id of UnaryExprOrTypeTraitExpr, second is id of type of argument
        llvm::errs() << Node << "_" << id << "->" << Node->getArgumentType().getTypePtr() << "_" << id << "\n";
    }

}


void ClangAstDumper::VisitDeclRefExpr(const DeclRefExpr *Node) {
    if(dumpStmt(Node)) {
        return;
    }

    // Dump qualifier
    if(Node->getQualifier() != nullptr) {
        // Can use the stream processor of decl ref expression qualifiers
        llvm::errs() << "DECL_REF_EXPR QUALIFIER BEGIN\n";
        llvm::errs() << getId(Node) << "\n";
        Node->getQualifier()->dump();
        llvm::errs() << "\nDECL_REF_EXPR QUALIFIER END\n";
    }

/*
    if(Node->hasExplicitTemplateArgs()) {
        llvm::errs() << "HAS_TEMPLATE_ARGS\n";
        llvm::errs() << getId(Node) << "\n";
        //DumpResources::template_args <<  D << "_" << id << "\n";
    }
*/
    // Dump template arguments
    if(Node->hasExplicitTemplateArgs()) {
        llvm::errs() << DUMP_TEMPLATE_ARGS << "\n";

        // Node id
        llvm::errs() << getId(Node) << "\n";

        // Number of template args
        llvm::errs() << Node->getNumTemplateArgs() << "\n";

        auto templateArgs = Node->getTemplateArgs();
        for (unsigned i = 0; i < Node->getNumTemplateArgs(); ++i) {
            auto templateArg = templateArgs + i;

            llvm::errs() << loc2str(templateArg->getSourceRange().getBegin(), templateArg->getSourceRange().getEnd()) << "\n";
        }

    }
}

void ClangAstDumper::VisitOffsetOfExpr(const OffsetOfExpr *Node) {
    if(dumpStmt(Node)) {
        return;
    }

    // Dump offset set
    const Type* offsetType = Node->getTypeSourceInfo()->getType().getTypePtr();
    dumpType(offsetType);

    llvm::errs() << DUMP_OFFSET_OF_INFO<< "\n";

    // Node id
    llvm::errs() << getId(Node) << "\n";



    // Offset type
    llvm::errs() << getId(offsetType) << "\n";

    // Number of expressions
    llvm::errs() << Node->getNumExpressions() << "\n";

    // Number of components
    int numComp = Node->getNumComponents();
    llvm::errs() << numComp << "\n";

    for(int i=0; i<numComp; i++) {

        llvm::errs() << Node->getComponent(i).getKind() << "\n";

        switch(Node->getComponent(i).getKind()) {
            case OffsetOfNode::Array:
                llvm::errs() << Node->getComponent(i).getArrayExprIndex() << "\n";
                break;
            case OffsetOfNode::Field:
                llvm::errs() << Node->getComponent(i).getFieldName()->getName() << "\n";
                break;
            case OffsetOfNode::Identifier:
                //llvm::errs() << "OFFSET FIELD NAME:" << Node->getComponent(i).getFieldName()->getName() << "\n";
                //break;
            case OffsetOfNode::Base:
                //llvm::errs() << "OFFSET BASE TYPE START\n";
                //Node->getComponent(i).getBase()->getType().dump();
                //llvm::errs() << "OFFSET BASE TYPE END\n";
                //break;
            default:
                // Do nothing, cases not yet implemented
                break;
        }




    }

}

void ClangAstDumper::VisitCXXDependentScopeMemberExpr(const CXXDependentScopeMemberExpr *Node) {
    if(dumpStmt(Node)) {
        return;
    }

    llvm::errs() << CXX_MEMBER_EXPR_INFO << "\n";

    // Node id
    llvm::errs() << getId(Node) << "\n";

    // Is arrow
    llvm::errs() << Node->isArrow() << "\n";

    // Member name
    llvm::errs() << Node->getMember().getAsString() << "\n";

}

void ClangAstDumper::VisitOverloadExpr(const OverloadExpr *Node, bool isTopCall) {
    if(isTopCall) {
        if (dumpStmt(Node)) {
            return;
        }
    }

    if(Node->getQualifier() != nullptr) {
        // Can use the stream processor of decl ref expression qualifiers
        llvm::errs() << "DECL_REF_EXPR QUALIFIER BEGIN\n";
        llvm::errs() << getId(Node) << "\n";
        Node->getQualifier()->dump();
        llvm::errs() << "\nDECL_REF_EXPR QUALIFIER END\n";
    }

}

void ClangAstDumper::VisitUnresolvedLookupExpr(const UnresolvedLookupExpr *Node) {

    if(dumpStmt(Node)) {
        return;
    }

    // Call parent in hierarchy
    VisitOverloadExpr(Node, false);
}

void ClangAstDumper::VisitUnresolvedMemberExpr(const UnresolvedMemberExpr *Node) {
    if(dumpStmt(Node)) {
        return;
    }

    // Call parent in hierarchy
    VisitOverloadExpr(Node, false);
}



void ClangAstDumper::VisitLambdaExpr(const LambdaExpr *Node) {
    if(dumpStmt(Node)) {
        return;
    }

//    llvm::errs() << "LAMBDA EXPR: " << getId(Node) << "\n";
//    llvm::errs() << "LAMBDA EXPR LAMBDA CLASS: " << getId(Node->getLambdaClass()) << "\n";

    // Visit Decl
    VisitDeclTop(Node->getLambdaClass());

    // Visit children
    // children() of LambdaExpr is not const?
    for (const Stmt *SubStmt : const_cast<LambdaExpr*>(Node)->children()) {
        if (SubStmt) {
            VisitStmtTop(SubStmt);
        }
    }

    // Dump lambda data
    llvm::errs() << LAMBDA_EXPR_DATA << "\n";
    llvm::errs() << getId(Node) << "\n";
    llvm::errs() << Node->isGenericLambda() << "\n";
    llvm::errs() << Node->isMutable() << "\n";
    llvm::errs() << Node->hasExplicitParameters() << "\n";
    llvm::errs() << Node->hasExplicitResultType() << "\n";
    llvm::errs() << Node->getCaptureDefault() <<"\n";
    // Number of captures
    llvm::errs() << Node->capture_size() <<"\n";
    // Info about each capture
    for(auto lambdaCapture : Node->captures()) {
        // Capture kind
        llvm::errs()  << lambdaCapture.getCaptureKind ()  << "\n";
    }


}

void ClangAstDumper::VisitSizeOfPackExpr(const SizeOfPackExpr *Node) {
    if(dumpStmt(Node)) {
        return;
    }

    // Visit pack
    VisitDeclTop(Node->getPack());

    // Map expr to pack?
}

void ClangAstDumper::VisitCXXUnresolvedConstructExpr(const CXXUnresolvedConstructExpr *Node) {
    if(dumpStmt(Node)) {
        return;
    }

    // Visit type as written
    VisitTypeTop(Node->getTypeAsWritten().getTypePtr());

    // Map current address to address of 'type as written'
    llvm::errs() << TYPE_AS_WRITTEN << "\n";
    llvm::errs() << getId(Node) << "\n";
    llvm::errs() << getId(Node->getTypeAsWritten().getTypePtr()) << "\n";
}

void ClangAstDumper::VisitCXXTypeidExpr(const CXXTypeidExpr *Node) {
    if(dumpStmt(Node)) {
        return;
    }

    bool isTypeOperand = Node->isTypeOperand();
    llvm::errs() << TYPEID_DATA << "\n";
    llvm::errs() << getId(Node) << "\n";

    llvm::errs() << isTypeOperand << "\n"; // True if is a type operand, false if it is an expression

    // Address of type/expr
    if(isTypeOperand) {
        QualType typeOperand = Node->getTypeOperand(*Context);
        llvm::errs() << getId(Node->getTypeOperand(*Context).getTypePtr()) << "\n";
        VisitTypeTop(typeOperand);
    } else {
        llvm::errs() << getId(Node->getExprOperand()) << "\n";
        VisitStmtTop(Node->getExprOperand());
    }
}

void ClangAstDumper::VisitInitListExpr(const InitListExpr *Node) {
    if(dumpStmt(Node)) {
        return;
    }

    llvm::errs() << INIT_LIST_EXPR_INFO << "\n";
    llvm::errs() << getId(Node) << "\n";

    // InitListExpr has method isExplicit(), but is not const
    bool isExplicit = Node->getLBraceLoc().isValid() && Node->getRBraceLoc().isValid();
    llvm::errs() << isExplicit << "\n";

}
