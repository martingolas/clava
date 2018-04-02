//
// Created by JoaoBispo on 20/01/2017.
//

#ifndef CLANGASTDUMPER_CLANGASTDUMPER_H
#define CLANGASTDUMPER_CLANGASTDUMPER_H

#include "ClavaDataDumper.h"

#include "clang/AST/TypeVisitor.h"
#include "clang/AST/StmtVisitor.h"
#include "clang/AST/DeclVisitor.h"

#include <set>
#include <string>
#include <vector>

//#define DEBUG

using namespace clang;


class ClangAstDumper : public TypeVisitor<ClangAstDumper>,  public ConstStmtVisitor<ClangAstDumper>, public ConstDeclVisitor<ClangAstDumper> {

private:
    ASTContext *Context;
    int id;

    std::set<const void*> seenTypes;
    std::set<const Stmt*> seenStmts;
    std::set<const Decl*> seenDecls;

    std::set<const CXXCtorInitializer*> seenInits;

    clava::ClavaDataDumper dataDumper;

public:
    explicit ClangAstDumper(ASTContext *Context, int id);

    void VisitTypeTop(const Type *T);
    void VisitTypeTop(const QualType& T);
    void VisitStmtTop(const Stmt *Node);
    void VisitDeclTop(const Decl *Node);


    /*
     * TYPES
     */
    void VisitType(const Type *T); // Should not be manually called, instead call VisitTypeTop()
    void VisitPointerType(const PointerType *T);
    void VisitTemplateSpecializationType(const TemplateSpecializationType *T);
    void VisitFunctionProtoType(const FunctionProtoType *T);
    void VisitTypedefType(const TypedefType *T);
    void VisitElaboratedType(const ElaboratedType *T);
    void VisitLValueReferenceType(const LValueReferenceType *T);
    void VisitDependentSizedArrayType(const DependentSizedArrayType *T);


        /*
         * STMTS
         */
    void VisitStmt(const Stmt *T); // Should not be manually called, instead call VisitStmtTop()
    void VisitDeclStmt(const DeclStmt *Node);
    void VisitCXXForRangeStmt(const CXXForRangeStmt *Node);
    void VisitCompoundStmt(const CompoundStmt *Node);
    void VisitForStmt(const ForStmt *Node);


    /*
     * EXPRS
     */
    void VisitUnaryExprOrTypeTraitExpr(const UnaryExprOrTypeTraitExpr *Node);
    void VisitCXXConstructExpr(const CXXConstructExpr *Node);
    void VisitDeclRefExpr(const DeclRefExpr *Node);
    void VisitOffsetOfExpr(const OffsetOfExpr *Node);
    void VisitCXXDependentScopeMemberExpr(const CXXDependentScopeMemberExpr *Node);
    void VisitOverloadExpr(const OverloadExpr *Node, bool isTopCall = true);
    void VisitUnresolvedLookupExpr(const UnresolvedLookupExpr *Node);
    void VisitUnresolvedMemberExpr(const UnresolvedMemberExpr *Node);
    void VisitLambdaExpr(const LambdaExpr *Node);
    void VisitSizeOfPackExpr(const SizeOfPackExpr *Node);
    void VisitCXXUnresolvedConstructExpr(const CXXUnresolvedConstructExpr *Node);
    void VisitCXXTypeidExpr(const CXXTypeidExpr *Node);
    void VisitInitListExpr(const InitListExpr *Node);
    void VisitCastExpr(const CastExpr *Node); // Works for ImplicitCastExpr, etc
    //void VisitImplicitCastExpr(const ImplicitCastExpr *Node);



    /*
     * DELCS
     */
    void VisitDecl(const Decl *D); // Should not be manually called, instead call VisitDeclTop()
    void VisitVarDecl(const VarDecl *D);
    void VisitFunctionDecl(const FunctionDecl *D);
    void VisitCXXMethodDecl(const CXXMethodDecl *D);
    void VisitCXXConstructorDecl(const CXXConstructorDecl *D);
    void VisitCXXConversionDecl(const CXXConversionDecl *D);
    void VisitCXXDestructorDecl(const CXXDestructorDecl *D);
    void VisitCXXRecordDecl(const CXXRecordDecl *D);
    void VisitObjCImplementationDecl(const ObjCImplementationDecl *D);
    void VisitTemplateDecl(const TemplateDecl *D);
    void VisitTemplateTypeParmDecl(const TemplateTypeParmDecl *D);
    void VisitNamespaceAliasDecl(const NamespaceAliasDecl *D);
    void VisitFieldDecl(const FieldDecl *D);
    void VisitParmVarDecl(const ParmVarDecl *D);
    void VisitTypedefDecl(const TypedefDecl *D);


    // Utility methods
    std::string loc2str(SourceLocation locStart, SourceLocation locEnd);
    std::string getId(const void* addr);
    std::string toBoolString(int value);
    const Type* getTypePtr(QualType T, std::string source);

    //void log(const char* name, const void* addr);
    void log(const Decl* D);
    void log(const Stmt* S);
    void log(const Type* T);



    // Private functions
private:

    void log(std::string name, const void* addr);


    // Children visitors
    void dumpVisitedChildren(const void *pointer, std::vector<std::string> children);


    void visitChildren(clava::DeclNode declNode, const Decl* D);
    void visitChildren(clava::StmtNode stmtNode, const Stmt* S);
    void visitChildren(clava::TypeNode typeNode, const Type* T);


    // Children visitors for Decls
    void VisitFunctionDeclChildren(const FunctionDecl *D, std::vector<std::string> &children);
    //void VisitCXXConstructorDeclChildren(const CXXConstructorDecl *D);
    void VisitCXXRecordDeclChildren(const CXXRecordDecl *D, std::vector<std::string> &children);

    void VisitVarDeclChildren(const VarDecl *D, std::vector<std::string> &children);
    void VisitParmVarDeclChildren(const ParmVarDecl *D, std::vector<std::string> &children);


    // Children visitors for Stmts
    void VisitStmtChildren(const Stmt *S, std::vector<std::string> &children);
    void VisitCompoundStmtChildren(const CompoundStmt *S, std::vector<std::string> &children);
    void VisitDeclStmtChildren(const DeclStmt *S, std::vector<std::string> &children);

    // Children visitors for Exprs
    void VisitCastExprChildren(const CastExpr *S, std::vector<std::string> &children);

    // Dumpers of other kinds of information
    void dumpIdToClassMap(const void* pointer, std::string className);


    /* Utility methods for DECLS */
    void dumpNumberTemplateParameters(const Decl *D, const TemplateParameterList *TPL);

    void dumpSourceRange(std::string id, SourceLocation startLoc, SourceLocation endLoc);

    // These methods return true if the node had been already visited

    bool dumpType(const Type* typeAddr);
    bool dumpType(const QualType& type);
    bool dumpStmt(const Stmt* stmtAddr);
    bool dumpDecl(const Decl* declAddr);


    /* EXTRA */
    void dumpCXXCtorInitializer(const CXXCtorInitializer *Init);




};


#endif //CLANGASTDUMPER_CLANGASTDUMPER_H
