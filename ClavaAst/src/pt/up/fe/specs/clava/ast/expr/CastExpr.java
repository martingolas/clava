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

import java.util.Arrays;
import java.util.Collection;

import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ClavaNodeInfo;
import pt.up.fe.specs.clava.ast.expr.data.ExprData;
import pt.up.fe.specs.clava.ast.expr.data.ValueKind;
import pt.up.fe.specs.clava.ast.type.Type;
import pt.up.fe.specs.clava.language.CastKind;

/**
 * Represents a type cast.
 * 
 * @author JoaoBispo
 *
 */
public abstract class CastExpr extends Expr {

    private final CastKind castKind;

    public CastExpr(CastKind castKind, ExprData exprData, ClavaNodeInfo info,
            Expr subExpr) {
        this(castKind, exprData, info, Arrays.asList(subExpr));
    }

    // protected CastExpr(CastKind castKind, ExprData exprData, ClavaNodeInfo info,
    // Expr subExpr, Collection<? extends ClavaNode> children) {
    //
    // this(castKind, exprData, info, CollectionUtils.concat(subExpr, children));
    // }

    /**
     * Constructor without children, for node copy.
     * 
     * @param castKind
     * @param location
     */
    // protected CastExpr(CastKind castKind, ExprData exprData, ClavaNodeInfo info) {
    // this(castKind, exprData, info, Collections.emptyList());
    // }

    protected CastExpr(CastKind castKind, ExprData exprData, ClavaNodeInfo info,
            Collection<? extends ClavaNode> children) {

        super(exprData, info, children);

        this.castKind = castKind;
    }

    @Override
    public ValueKind getValueKind() {
        return getSubExpr().getValueKind();
    }

    public CastKind getCastKind() {
        return castKind;
    }

    public Expr getSubExpr() {
        return getChild(Expr.class, 0);
    }

    /**
     * The type this cast will cast to.
     * 
     * @return
     */
    public Type getCastType() {
        return getType();
    }

}
