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

package pt.up.fe.specs.clava.ast.decl.data;

import pt.up.fe.specs.util.enums.EnumHelper;
import pt.up.fe.specs.util.lazy.Lazy;
import pt.up.fe.specs.util.providers.StringProvider;

/**
 * Storage class specifiers.
 * 
 * @author JoaoBispo
 *
 */
public enum StorageClass implements StringProvider {
    NONE,
    AUTO,
    EXTERN,
    PRIVATE_EXTERN("__private_extern__"),
    REGISTER,
    STATIC;

    private static final Lazy<EnumHelper<StorageClass>> HELPER = EnumHelper.newLazyHelper(StorageClass.class, NONE);

    public static EnumHelper<StorageClass> getHelper() {
        return HELPER.get();
    }

    private final String name;

    private StorageClass() {
        this.name = name().toLowerCase();
    }

    private StorageClass(String name) {
        this.name = name;
    }

    /**
     * The code for this Storage class (e.g., extern, static...)
     */
    @Override
    public String getString() {
        return name;
    }
}
