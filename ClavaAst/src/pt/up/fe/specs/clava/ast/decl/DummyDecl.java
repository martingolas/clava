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

package pt.up.fe.specs.clava.ast.decl;

import java.util.Collection;

import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ClavaNodeInfo;
import pt.up.fe.specs.clava.ClavaNodes;
import pt.up.fe.specs.clava.ast.DummyNode;
import pt.up.fe.specs.clava.ast.decl.data.DeclData;
import pt.up.fe.specs.clava.ast.decl.data2.DeclDataV2;

/**
 * Dummy declaration, for testing purposes.
 * 
 * @author JoaoBispo
 *
 */
public class DummyDecl extends Decl implements DummyNode {

    private final String classname;

    public DummyDecl(String classname, DeclDataV2 clavaData, Collection<? extends ClavaNode> children) {
        super(clavaData, children);

        this.classname = classname;
    }

    protected DummyDecl(ClavaNodeInfo info, Collection<? extends ClavaNode> children) {
        super(DeclData.empty(), info, children);

        classname = null;
    }

    @Override
    public String getNodeName() {
        return super.getNodeName() + " (" + classname + ")";
    }

    public String getNodeCode() {
        return "/* Dummy declaration '" + classname + "' */";
    }

    @Override
    public String getCode() {
        return ClavaNodes.toCode(getNodeCode(), this);
    }

    @Override
    public String getContent() {
        return getData().toString();
    }

    /*
    @Override
    protected ClavaNode copyPrivate() {
        return new DummyDecl(content, getInfo(), Collections.emptyList());
    }
    */

    /*
    @Override
    public String getContent() {
        return content;
    }
    */
    /*
    @Override
    public String toContentString() {
        return super.toContentString() + " " + getLocation();
    }
    */
}
