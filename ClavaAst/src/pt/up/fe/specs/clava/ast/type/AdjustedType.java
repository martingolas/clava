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

package pt.up.fe.specs.clava.ast.type;

import java.util.Arrays;
import java.util.Collection;

import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ClavaNodeInfo;
import pt.up.fe.specs.clava.ast.type.data.TypeData;

/**
 * Type that was implicitly adjusted.
 * 
 * @author JoaoBispo
 *
 */
public abstract class AdjustedType extends Type {

    protected AdjustedType(TypeData typeData, ClavaNodeInfo info, Type originalType, Type adjustedType) {
        this(typeData, info, Arrays.asList(originalType, adjustedType));
    }

    protected AdjustedType(TypeData typeData, ClavaNodeInfo info, Collection<? extends ClavaNode> children) {
        super(typeData, info, children);
    }

    public abstract Type getAdjustedType();

}
