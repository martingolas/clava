//
// Created by JoaoBispo on 01/04/2018.
//

#include "ClangAstDumper.h"
#include "ClavaConstants.h"
#include "ClangNodes.h"

#include <string>

const std::map<const std::string, clava::StmtNode > ClangAstDumper::STMT_CHILDREN_MAP = {
        {"DeclStmt", clava::StmtNode::DECL_STMT}
};

const std::map<const std::string, clava::StmtNode > ClangAstDumper::EXPR_CHILDREN_MAP = {

};

void ClangAstDumper::visitChildren(const Stmt* S) {
    // Get classname
    const std::string classname = clava::getClassName(S);

    // Get corresponding StmtNode
    clava::StmtNode stmtNode = STMT_CHILDREN_MAP.count(classname) == 1 ? STMT_CHILDREN_MAP.find(classname)->second :
                               clava::StmtNode::STMT;

    visitChildren(stmtNode, S);
}

void ClangAstDumper::visitChildren(const Expr* E) {
    // Get classname
    const std::string classname = clava::getClassName(E);

    // Get corresponding ExprNode
    clava::StmtNode exprNode = EXPR_CHILDREN_MAP.count(classname) == 1 ? EXPR_CHILDREN_MAP.find(classname)->second :
                               clava::StmtNode::EXPR;

    visitChildren(exprNode, E);
}

void ClangAstDumper::visitChildren(clava::StmtNode stmtNode, const Stmt* S) {

    std::vector<std::string> visitedChildren;

    switch(stmtNode) {
        case clava::StmtNode::STMT:
            VisitStmtChildren(S, visitedChildren); break;
//        case clava::StmtNode::COMPOUND_STMT:
//            VisitCompoundStmtChildren(static_cast<const CompoundStmt *>(S), visitedChildren); break;
        case clava::StmtNode::DECL_STMT:
            VisitDeclStmtChildren(static_cast<const DeclStmt *>(S), visitedChildren); break;


        case clava::StmtNode::EXPR:
            VisitExprChildren(static_cast<const Expr *>(S), visitedChildren); break;
//        case clava::StmtNode::CAST_EXPR:
//            VisitCastExprChildren(static_cast<const CastExpr *>(S), visitedChildren); break;


        default: throw std::invalid_argument("ChildrenVisitorStmts::visitChildren(StmtNode): Case not implemented, '"+clava::getName(stmtNode)+"'");

    }

    dumpVisitedChildren(S, visitedChildren);
}

void ClangAstDumper::VisitStmtChildren(const Stmt *S, std::vector<std::string> &children) {
    // Visit Stmt children
    for (const Stmt *SubStmt : S->children()) {
        if (SubStmt) {
            VisitStmtTop(SubStmt);
            children.push_back(clava::getId(SubStmt, id));
        }
    }
}


/*
void ClangAstDumper::VisitCompoundStmtChildren(const CompoundStmt *S, std::vector<std::string> &children) {

    // Visit sub-statements
    for (auto &Arg : S->body()) {
        VisitStmtTop(Arg);
        children.push_back(getId(Arg));
    }
}
 */


void ClangAstDumper::VisitDeclStmtChildren(const DeclStmt *S, std::vector<std::string> &children) {
    // Do not visit sub-statements, only decls
    //VisitStmtChildren(S, children);

    // Visit decls
    for (DeclStmt::const_decl_iterator I = S->decl_begin(), E = S->decl_end(); I != E; ++I) {

        VisitDeclTop(*I);
        children.push_back(clava::getId(*I, id));
    }

}

void ClangAstDumper::VisitExprChildren(const Expr *E, std::vector<std::string> &children) {
    // Visit sub-statements
    VisitStmtChildren(E, children);

    // Visit type
    VisitTypeTop(E->getType());
    dumpTopLevelType(E->getType());
}


/*
void ClangAstDumper::VisitCastExprChildren(const CastExpr *S, std::vector<std::string> &children) {

    // Sub-expression
    auto subExprAsWritten = S->getSubExprAsWritten();
    VisitStmtTop(subExprAsWritten);
    children.push_back(getId(subExprAsWritten));
}
 */