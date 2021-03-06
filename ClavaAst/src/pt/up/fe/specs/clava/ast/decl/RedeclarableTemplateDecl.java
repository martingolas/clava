/**
 * Copyright 2017 SPeCS.
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import pt.up.fe.specs.clava.ClavaNode;
import pt.up.fe.specs.clava.ClavaNodeInfo;
import pt.up.fe.specs.clava.ast.decl.data.DeclData;
import pt.up.fe.specs.util.SpecsCollections;

/**
 * Declaration of a redeclarable template.
 * 
 * @author JoaoBispo
 *
 */
public class RedeclarableTemplateDecl extends TemplateDecl {

    public RedeclarableTemplateDecl(String declName, List<Decl> specializations, DeclData declData, ClavaNodeInfo info,
            List<NamedDecl> templateParameters, Decl templateDecl) {

        this(declName, specializations, declData, info, SpecsCollections.concat(templateParameters, templateDecl));
    }

    protected RedeclarableTemplateDecl(String declName, List<Decl> specializations, DeclData declData,
            ClavaNodeInfo info, Collection<? extends ClavaNode> children) {

        super(declName, specializations, declData, info, children);
    }

    @Override
    protected ClavaNode copyPrivate() {
        return new RedeclarableTemplateDecl(getDeclName(), getSpecializations(), getDeclData(), getInfo(),
                Collections.emptyList());
    }

    @Override
    public String getCode() {
        StringBuilder code = new StringBuilder();

        code.append("template <");

        String parameterList = getTemplateParameters().stream()
                .map(param -> param.getCode())
                .collect(Collectors.joining(", "));

        code.append(parameterList).append(">");
        code.append(getTemplateDecl().getCode());

        return code.toString();

    }

}
