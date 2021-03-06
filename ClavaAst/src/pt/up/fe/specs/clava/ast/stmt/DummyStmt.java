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

package pt.up.fe.specs.clava.ast.stmt;

import java.util.Collection;
import java.util.Collections;

import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ClavaNodeInfo;
import pt.up.fe.specs.clava.ClavaNodes;
import pt.up.fe.specs.clava.ast.DummyNode;

/**
 * Dummy statement, for testing purposes.
 * 
 * @author JoaoBispo
 *
 */
public class DummyStmt extends Stmt implements DummyNode {

    private final String content;

    public DummyStmt(String content, ClavaNodeInfo info, Collection<? extends ClavaNode> children) {
        super(info, children);

        this.content = content;
    }

    public String getNodeCode() {
        return "/* Dummy statement '" + content + "'*/";
    }

    @Override
    public String getCode() {
        return ClavaNodes.toCode(getNodeCode(), this);
    }

    @Override
    protected ClavaNode copyPrivate() {
        return new DummyStmt(content, getInfo(), Collections.emptyList());
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String toContentString() {
        return super.toContentString() + "location:" + getLocation();
    }

}
