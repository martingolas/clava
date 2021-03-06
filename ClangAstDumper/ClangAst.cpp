//------------------------------------------------------------------------------
//
// Created by JoaoBispo
//
// Based on public domain code by Eli Bendersky (eliben@gmail.com) - http://eli.thegreenplace.net/
//------------------------------------------------------------------------------
#include "ClangAst.h"

#include "clang/AST/AST.h"
#include "clang/AST/ASTConsumer.h"
#include "clang/AST/RecursiveASTVisitor.h"
#include "clang/Frontend/ASTConsumers.h"
#include "clang/Frontend/FrontendActions.h"
#include "clang/Frontend/CompilerInstance.h"
#include "clang/AST/Comment.h"
#include "clang/AST/Stmt.h"
#include "clang/Lex/Lexer.h"
#include "clang/Lex/Preprocessor.h"


#include <iostream>
#include <fstream>
#include <atomic>
#include <sstream>
#include <string>



using namespace clang;

static llvm::cl::OptionCategory ToolingSampleCategory("Tooling Sample");

static constexpr const char* const PREFIX = "COUNTER";


/* DumpAstVisitor implementation */

bool DumpAstVisitor::TraverseDecl(Decl *D) {
    FullSourceLoc fullLocation = Context->getFullLoc(D->getLocStart());
    if (fullLocation.isValid() && !fullLocation.isInSystemHeader()) {
        (*D).dump(llvm::outs());
    }

    return false;

}

PrintNodesTypesRelationsVisitor::PrintNodesTypesRelationsVisitor(ASTContext *Context, int id) : Context(Context), id(id), dumper(Context, id)  {};

static std::string stmt2str(clang::Stmt *d, clang::SourceManager *sm, clang::LangOptions lopt) {
    clang::SourceLocation b(d->getLocStart()), _e(d->getLocEnd());
    clang::SourceLocation e(clang::Lexer::getLocForEndOfToken(_e, 0, *sm, lopt));
    return std::string(sm->getCharacterData(b),
                       sm->getCharacterData(e)-sm->getCharacterData(b));
}

    static std::string loc2str(SourceLocation locStart, SourceLocation locEnd, ASTContext *Context) {
        clang::SourceManager *sm = &Context->getSourceManager();
        clang::LangOptions lopt = Context->getLangOpts();

        clang::SourceLocation b(locStart), _e(locEnd);
        clang::SourceLocation e(clang::Lexer::getLocForEndOfToken(_e, 0, *sm, lopt));
        return std::string(sm->getCharacterData(b), sm->getCharacterData(e)-sm->getCharacterData(b));
    }



    bool PrintNodesTypesRelationsVisitor::VisitOMPExecutableDirective(OMPExecutableDirective * D) {

        FullSourceLoc fullLocation = Context->getFullLoc(D->getLocStart());

        if (!fullLocation.isInSystemHeader()) {
            // Get OMP Directive
            DumpResources::omp << D << "_" << id << "->" << stmt2str(D, &Context->getSourceManager(), Context->getLangOpts()) << "\n";
            DumpResources::omp << "NUM_CLAUSES" << "->" << D->getNumClauses() << "\n";
            for(int i=0; i<D->getNumClauses(); i++) {
                OMPClause* clause = D->getClause(i);
                DumpResources::omp << clause->getClauseKind() << "->" << loc2str(clause->getLocStart(), clause->getLocEnd(), Context) << "\n";
            }
        }

        return true;
    }


/*
    bool PrintNodesTypesRelationsVisitor::VisitDeclRefExpr(DeclRefExpr * D) {

        FullSourceLoc fullLocation = Context->getFullLoc(D->getLocStart());

        if (!fullLocation.isInSystemHeader()) {
            if (D->getQualifier() != nullptr) {
                llvm::errs() << "DECL_REF_EXPR QUALIFIER BEGIN\n";
                llvm::errs() << D << "_" << id << "\n";
                D->getQualifier()->dump();
                llvm::errs() << "\nDECL_REF_EXPR QUALIFIER END\n";
            }


            if(D->hasExplicitTemplateArgs()) {
                DumpResources::template_args <<  D << "_" << id << "\n";
            }

        }
        return true;
    }
*/


    bool PrintNodesTypesRelationsVisitor::VisitCXXConstructExpr(CXXConstructExpr * D) {

        FullSourceLoc fullLocation = Context->getFullLoc(D->getLocStart());
        if (!fullLocation.isInSystemHeader()) {

            // Check if temporary object
            if(D->isTemporaryObject(D->getConstructor()->getASTContext(), D->getConstructor()->getParent())) {
                DumpResources::is_temporary <<  D << "_" << id << "\n";
            }

        }


        return true;
    }

    // Dump types for Expr, TypeDecl and ValueDecl, as well as the connection between them
    bool PrintNodesTypesRelationsVisitor::VisitExpr(Expr *D) {

        FullSourceLoc fullLocation = Context->getFullLoc(D->getLocStart());
        if (!fullLocation.isInSystemHeader()) {
            //if(D->getType().isNull()) {
            //    llvm::errs() << "VisitExpr null type\n";
            //}
            dumpNodeToType(DumpResources::nodetypes,D, D->getType());

        }

        return true;
    }

    bool PrintNodesTypesRelationsVisitor::VisitTypeDecl(TypeDecl *D) {


        FullSourceLoc fullLocation = Context->getFullLoc(D->getLocStart());
        if (!fullLocation.isInSystemHeader()) {
            //llvm::errs() << "Visiting Type Decl: " << D << "\n";
            dumpNodeToType(DumpResources::nodetypes, D, D->getTypeForDecl());
        }

        return true;
    }

    /**
     * Typedefs will be visited by 'VisitTypeDecl' but will null, this override extracts the correct information for typedefs
     * @param D
     * @return
     */
    bool PrintNodesTypesRelationsVisitor::VisitTypedefNameDecl(TypedefNameDecl *D) {
        FullSourceLoc fullLocation = Context->getFullLoc(D->getLocStart());
        if (!fullLocation.isInSystemHeader()) {
            //if(D->getUnderlyingType().isNull()) {
            //    llvm::errs() << "VisitNameDecl null type\n";
            //}
            dumpNodeToType(DumpResources::nodetypes, D, D->getUnderlyingType());

        }

        return true;
    }


    bool PrintNodesTypesRelationsVisitor::VisitEnumDecl(EnumDecl *D) {
        FullSourceLoc fullLocation = Context->getFullLoc(D->getLocStart());
        if (!fullLocation.isInSystemHeader()) {
            //if(D->getIntegerType().isNull()) {
            //    llvm::errs() << "VisitEnum null type\n";
            //}
            dumpNodeToType(DumpResources::enum_integer_type, D,D->getIntegerType(), false);

        }

        return true;
    }



    bool PrintNodesTypesRelationsVisitor::VisitValueDecl(ValueDecl *D) {

        FullSourceLoc fullLocation = Context->getFullLoc(D->getLocStart());
        if (!fullLocation.isInSystemHeader()) {
            //llvm::errs() << "Visiting Value Decl: " << D << "\n";
            //if(D->getType().isNull()) {
            //    llvm::errs() << "VisitValueDecl null type\n";
            //}
            dumpNodeToType(DumpResources::nodetypes, D, D->getType());

            return true;
        }

        return true;
    }

    // Visit only nodes from the source code, ignore system headers
    bool PrintNodesTypesRelationsVisitor::VisitDecl(Decl *D) {
        FullSourceLoc fullLocation = Context->getFullLoc(D->getLocStart());

        if (!fullLocation.isInSystemHeader()) {
            dumper.VisitDeclTop(D);
            return true;
        }

        return true;
    }

    bool PrintNodesTypesRelationsVisitor::VisitStmt(Stmt *D) {
        FullSourceLoc fullLocation = Context->getFullLoc(D->getLocStart());

        if (!fullLocation.isInSystemHeader()) {
            dumper.VisitStmtTop(D);
            return true;
        }

        return true;
    }

    bool PrintNodesTypesRelationsVisitor::VisitLambdaExpr(LambdaExpr *D) {
        FullSourceLoc fullLocation = Context->getFullLoc(D->getLocStart());

        if (!fullLocation.isInSystemHeader()) {
            // Dump self
            dumpNodeToType(DumpResources::nodetypes,D, D->getType());

            // Traverse lambda class
            TraverseCXXRecordDecl(D->getLambdaClass());
            //TraverseDecl(D->getLambdaClass());
            //VisitTypeDecl(D->getLambdaClass());
        }

        return true;
    }


void PrintNodesTypesRelationsVisitor::dumpNodeToType(std::ofstream &stream, void* nodeAddr, const QualType& type, bool checkDuplicates) {

    // Opaque pointer, so that we can obtain the qualifiers
    void* typeAddr = !type.isNull() ? type.getAsOpaquePtr() : nullptr;
    //const Type* typeAddr = type.getTypePtrOrNull();

    if(checkDuplicates) {
        if(seenNodes.count(nodeAddr) == 0) {
            stream << nodeAddr << "_" << id << "->" << typeAddr << "_" << id << "\n";
            seenNodes.insert(nodeAddr);
        }
    } else {
        stream << nodeAddr << "_" << id << "->" << typeAddr << "_" << id << "\n";
    }


    dumper.VisitTypeTop(type);
}


 void PrintNodesTypesRelationsVisitor::dumpNodeToType(std::ofstream &stream, void* nodeAddr, const Type* typeAddr, bool checkDuplicates) {

    if(!typeAddr) {
        return;
    }


    if(checkDuplicates) {
        if(seenNodes.count(nodeAddr) == 0) {
            stream << nodeAddr << "_" << id << "->" << typeAddr << "_" << id << "\n";
            seenNodes.insert(nodeAddr);
        }
    } else {
        stream << nodeAddr << "_" << id << "->" << typeAddr << "_" << id << "\n";
    }

    dumper.VisitTypeTop(typeAddr);
}


    MyASTConsumer::MyASTConsumer(ASTContext *C, int id) : topLevelDeclVisitor(C, id), printRelationsVisitor(C, id), id(id) {
        std::ofstream consumer;
        consumer.open("consumer_order.txt", std::ofstream::app);
        consumer << "ASTConsumer built " << id << "\n";
        consumer.close();
    }

    MyASTConsumer::~MyASTConsumer() {
        std::ofstream consumer;
        consumer.open("consumer_order.txt", std::ofstream::app);
        consumer << "ASTConsumer destroyed " << id << "\n";
        consumer.close();
    }




    // Override the method that gets called for each parsed top-level declaration.
    bool MyASTConsumer::HandleTopLevelDecl(DeclGroupRef DR) {

        for (DeclGroupRef::iterator b = DR.begin(), e = DR.end(); b != e; ++b) {
            // Traverse the declaration using our AST visitor.
            topLevelDeclVisitor.TraverseDecl(*b);
        }


        for (DeclGroupRef::iterator b = DR.begin(), e = DR.end(); b != e; ++b) {
            printRelationsVisitor.TraverseDecl(*b);
        }

        return true;
    }




    // Not sure if Clang performs compilation of different translation units in parallel, using atomic just in case.
    std::atomic<int> GLOBAL_COUNTER(1);



    /*** DumpAstAction ***/

    // For each source file provided to the tool, a new FrontendAction is created.
    std::unique_ptr<ASTConsumer> DumpAstAction::CreateASTConsumer(CompilerInstance &CI, StringRef file) {
        int counter = GLOBAL_COUNTER.fetch_add(1);
        DumpResources::writeCounter(counter);

        return llvm::make_unique<MyASTConsumer>(&CI.getASTContext(), counter);
    }



    /*** DumpIncludesAction ***/
	// Based on explanation from this website https://xaizek.github.io/2015-04-23/detecting-wrong-first-include/
    void DumpIncludesAction::ExecuteAction() {

        IncludeDumper includeDumper(getCompilerInstance());
        getCompilerInstance().getPreprocessor().addPPCallbacks(
                includeDumper.createPreprocessorCallbacks()
        );

        PreprocessOnlyAction::ExecuteAction();
    }



    /*** IncludeDumper ***/

    IncludeDumper::IncludeDumper(CompilerInstance &compilerInstance) : compilerInstance(compilerInstance) {};


    std::unique_ptr<PPCallbacks> IncludeDumper::createPreprocessorCallbacks() {
        return std::unique_ptr<PPCallbacks>(new CallbacksProxy(*this));
    }

    void IncludeDumper::InclusionDirective(SourceLocation HashLoc, const Token &IncludeTok, StringRef FileName,
                                        bool IsAngled, CharSourceRange FilenameRange, const FileEntry *File,
                                        StringRef SearchPath, StringRef RelativePath, const Module *Imported) {


        clang::SourceManager &sm = compilerInstance.getSourceManager();

        if (!sm.isInSystemHeader(HashLoc)) {

            DumpResources::includes << "source:" << sm.getFilename(HashLoc).str() << "|";
            DumpResources::includes << "include:" << FileName.str() << "|";
            DumpResources::includes << "line:" << sm.getSpellingLineNumber(HashLoc) << "|";
            DumpResources::includes << "angled:" << IsAngled ? "true" : "false";
            DumpResources::includes << "\n";
        }
    }




    void IncludeDumper::MacroExpands(const Token & MacroNameTok, const MacroDefinition & MD, SourceRange Range, const MacroArgs * Args) {
/*
        llvm::errs() << "Dumping Macro info:\n";
        MD.getMacroInfo()->dump();
        llvm::errs() << "\n";
        //llvm::errs() << "\nDumping Macro info end\n";


        llvm::errs() << "Definition begin:\n";
        MD.getMacroInfo()->getDefinitionLoc().dump(compilerInstance.getSourceManager());
        llvm::errs() << "\nDefinition length: " << MD.getMacroInfo()->getDefinitionLength(compilerInstance.getSourceManager()) <<"\n";
        llvm::errs() << "\n";
        //llvm::errs() << "\nDefinition begin end\n";

        llvm::errs() << "Expansion begin:\n";
        Range.getBegin().dump(compilerInstance.getSourceManager());
        llvm::errs() << "\n";

        */



/*
        llvm::errs() << "Is id: " << MacroNameTok.isAnyIdentifier() << "\n";
        if(MacroNameTok.isAnyIdentifier()) {
            llvm::errs() << "RAW id: " << MacroNameTok.getRawIdentifier().str() << "\n";
        }
        llvm::errs() << "Kind: " << MacroNameTok.getKind() << "\n";
        */
 //       llvm::errs() << "ID INFO: " << MacroNameTok. << "\n";
//       llvm::errs() << "Token begin:\n";
  //      MacroNameTok.getLocation().dump(compilerInstance.getSourceManager());
    //    llvm::errs() << "\nToken end\n";


        //llvm::errs() << "\nSource begin end\n";

/*
        llvm::errs() << "Source end:\n";
        Range.getEnd().dump(compilerInstance.getSourceManager());
        llvm::errs() << "\nSource end end\n";
*/
/*
        if(MacroNameTok.isLiteral()) {
            llvm::errs() << "Token literal: " << MacroNameTok.getLiteralData() << "\n";
        }
*/
    }



    /*** CallbacksProxy ***/

    CallbacksProxy::CallbacksProxy(IncludeDumper &original) : original(original) {
    }

    void CallbacksProxy::InclusionDirective(SourceLocation HashLoc, const Token &IncludeTok, StringRef FileName,
                                            bool IsAngled, CharSourceRange FilenameRange, const FileEntry *File,
                                            StringRef SearchPath, StringRef RelativePath, const Module *Imported) {
												
        original.InclusionDirective(HashLoc, IncludeTok, FileName, IsAngled, FilenameRange, File, SearchPath, RelativePath, Imported);
    }

    void CallbacksProxy::MacroExpands(const Token & MacroNameTok, const MacroDefinition & MD, SourceRange Range, const MacroArgs * Args) {
        original.MacroExpands(MacroNameTok, MD, Range, Args);
    }



/**
 * DumpResources Implementations
 */

// File instantiations
std::ofstream DumpResources::includes;
std::ofstream DumpResources::nodetypes;
//std::ofstream DumpResources::template_args;
std::ofstream DumpResources::is_temporary;
std::ofstream DumpResources::omp;
std::ofstream DumpResources::enum_integer_type;
std::ofstream DumpResources::consumer_order;
std::ofstream DumpResources::types_with_templates;


void DumpResources::writeCounter(int id) {

    // Output is processed with a line iterator, allows multiple-line processing

    llvm::outs() << PREFIX << "\n";
    llvm::outs() << id << "\n";

    llvm::errs() << PREFIX << "\n";
    llvm::errs() << id << "\n";

}

void DumpResources::init() {

    // Clear files
    DumpResources::includes.open("includes.txt", std::ofstream::out | std::fstream::trunc);
    DumpResources::nodetypes.open("nodetypes.txt", std::ofstream::out | std::ofstream::trunc);
    //DumpResources::template_args.open("template_args.txt", std::ofstream::out | std::ofstream::trunc);
    DumpResources::is_temporary.open("is_temporary.txt", std::ofstream::out | std::ofstream::trunc);
    DumpResources::omp.open("omp.txt", std::ofstream::out | std::ofstream::trunc);
    DumpResources::enum_integer_type.open("enum_integer_type.txt", std::ofstream::out | std::ofstream::trunc);
    DumpResources::consumer_order.open("consumer_order.txt", std::ofstream::out | std::ofstream::trunc);
    DumpResources::types_with_templates.open("types_with_templates.txt", std::ofstream::out | std::ofstream::trunc);
}

void DumpResources::finish() {
    DumpResources::includes.close();
    DumpResources::nodetypes.close();
    //DumpResources::template_args.close();
    DumpResources::is_temporary.close();
    DumpResources::omp.close();
    DumpResources::enum_integer_type.close();
    DumpResources::consumer_order.close();
    DumpResources::types_with_templates.close();
}

