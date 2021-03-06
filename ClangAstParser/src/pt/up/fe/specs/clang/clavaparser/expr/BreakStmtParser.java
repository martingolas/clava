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

package pt.up.fe.specs.clang.clavaparser.expr;

import pt.up.fe.specs.clang.ast.ClangNode;
import pt.up.fe.specs.clang.clavaparser.AClangNodeParser;
import pt.up.fe.specs.clang.clavaparser.ClangConverterTable;
import pt.up.fe.specs.clava.ast.ClavaNodeFactory;
import pt.up.fe.specs.clava.ast.stmt.BreakStmt;
import pt.up.fe.specs.util.stringparser.StringParser;

public class BreakStmtParser extends AClangNodeParser<BreakStmt> {

    public BreakStmtParser(ClangConverterTable converter) {
        super(converter, false);
    }

    @Override
    protected BreakStmt parse(ClangNode node, StringParser parser) {

        checkNoChildren(node);

        return ClavaNodeFactory.breakStmt(node.getInfo());
    }

}
