/**
 * Copyright 2016 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.clava.ast.expr;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ClavaNodeInfo;
import pt.up.fe.specs.clava.ClavaNodes;
import pt.up.fe.specs.clava.ast.decl.VarDecl;
import pt.up.fe.specs.clava.ast.expr.data.CXXConstructExprData;
import pt.up.fe.specs.clava.ast.expr.data.ExprData;
import pt.up.fe.specs.clava.ast.expr.data.ValueKind;
import pt.up.fe.specs.clava.ast.type.NullType;

/**
 * Represents a call to a C++ constructor.
 * 
 * @author JoaoBispo
 *
 */
public class CXXConstructExpr extends Expr {

    private final CXXConstructExprData constructorData;

    public CXXConstructExpr(CXXConstructExprData constructorData, ExprData exprData,
            ClavaNodeInfo info, Collection<? extends Expr> args) {

        super(exprData, info, args);

        this.constructorData = constructorData;
    }

    @Override
    protected ClavaNode copyPrivate() {
        return new CXXConstructExpr(getConstructorData(), getExprData(), getInfo(), Collections.emptyList());
    }

    public CXXConstructExprData getConstructorData() {
        return constructorData;
    }

    public boolean isElidable() {
        return constructorData.isElidable();
    }

    /**
     * 
     * @return the list of arguments, including default arguments added by the parser
     */
    public List<Expr> getCompleteArgs() {
        return getChildren(Expr.class);
    }

    /**
     * 
     * @return the list of arguments that should appear in the code
     */
    public List<Expr> getArgs() {
        return getCompleteArgs().stream()
                .filter(arg -> !(arg instanceof CXXDefaultArgExpr))
                .collect(Collectors.toList());
    }

    @Override
    public String getCode() {
        String typeCode = getExprType() instanceof NullType ? null : getExprType().getCode();
        return getCode(typeCode);
        // return getCode(getExprType().getCode());
    }

    public String getCode(String cxxRecordName) {
        if (cxxRecordName == null) {
            return "";
        }

        // Special case: constructor that receives an initializer_list
        if (cxxRecordName.startsWith("initializer_list<")) {
            return getArgs().stream()
                    .map(arg -> arg.getCode())
                    .collect(Collectors.joining(", ", "{", "}"));
        }

        String argsCode = getArgsCode();

        // Special case: No arguments before VarDecl
        if (argsCode.isEmpty() &&
                ClavaNodes.getParentNormalized(this) instanceof VarDecl) {
            // return cxxRecordName + "{}";
            return "{}";
        }

        return cxxRecordName + argsCode;
    }

    public String getArgsCode() {

        String argsCode = getArgs().stream()
                .map(arg -> arg.getCode())
                .collect(Collectors.joining(", "));

        if (argsCode.isEmpty()) {
            // HACK: Probably this is not correct
            if (isTemporary()) {
                return "()";
            }
            return "";
        }

        return "(" + argsCode + ")";
    }

    @Override
    public ValueKind getValueKind() {
        // As default, return r-value
        return ValueKind.R_VALUE;
    }

    protected boolean isTemporary() {
        return false;
    }

    @Override
    public String toContentString() {
        return ClavaNode.toContentString(super.toContentString(), constructorData.toString());
    }

}
