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

public class LabelStmt extends Stmt {

    private final String label;

    public LabelStmt(String label, ClavaNodeInfo info) {
	this(label, info, Collections.emptyList());
    }

    private LabelStmt(String label, ClavaNodeInfo info, Collection<? extends ClavaNode> children) {
	super(info, children);

	this.label = label;
    }

    @Override
    protected ClavaNode copyPrivate() {
	return new LabelStmt(label, getInfo());
    }

    public String getLabel() {
	return label;
    }

    @Override
    public String getCode() {
	return label + ":";
    }

}
