/**
 * Copyright 2018 SPeCS.
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

package pt.up.fe.specs.clang.parsers.data;

import java.math.BigInteger;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.clang.parsers.ClavaNodes;
import pt.up.fe.specs.clang.parsers.GeneralParsers;
import pt.up.fe.specs.clang.parsers.NodeDataParser;
import pt.up.fe.specs.clava.ast.expr.CXXBoolLiteralExpr;
import pt.up.fe.specs.clava.ast.expr.CastExpr;
import pt.up.fe.specs.clava.ast.expr.CharacterLiteral;
import pt.up.fe.specs.clava.ast.expr.Expr;
import pt.up.fe.specs.clava.ast.expr.FloatingLiteral;
import pt.up.fe.specs.clava.ast.expr.IntegerLiteral;
import pt.up.fe.specs.clava.ast.expr.Literal;
import pt.up.fe.specs.clava.ast.expr.enums.CharacterKind;
import pt.up.fe.specs.clava.ast.expr.enums.ObjectKind;
import pt.up.fe.specs.clava.ast.expr.enums.ValueKind;
import pt.up.fe.specs.clava.language.CastKind;
import pt.up.fe.specs.util.utilities.LineStream;

/**
 * ClavaData parsers for Expr nodes.
 * 
 * 
 * @author JoaoBispo
 *
 */
public class ExprDataParser {

    public static DataStore parseExprData(LineStream lines, DataStore dataStore) {

        DataStore data = NodeDataParser.parseNodeData(lines, dataStore);
        // TODO: ClavaNodes.getType, should be in ClavaContext?
        data.add(Expr.TYPE, ClavaNodes.getType(dataStore, lines.nextLine()));
        data.add(Expr.VALUE_KIND, GeneralParsers.enumFromInt(ValueKind.getEnumHelper(), lines));
        data.add(Expr.OBJECT_KIND, GeneralParsers.enumFromInt(ObjectKind.getEnumHelper(), lines));

        return data;
    }

    public static DataStore parseCastExprData(LineStream lines, DataStore dataStore) {
        DataStore data = parseExprData(lines, dataStore);

        data.add(CastExpr.CAST_KIND, GeneralParsers.enumFromName(CastKind.getHelper(), lines));

        return data;
    }

    public static DataStore parseLiteralData(LineStream lines, DataStore dataStore) {
        DataStore data = parseExprData(lines, dataStore);

        data.add(Literal.SOURCE_LITERAL, lines.nextLine());

        return data;
    }

    public static DataStore parseCharacterLiteralData(LineStream lines, DataStore dataStore) {
        DataStore data = parseLiteralData(lines, dataStore);

        data.add(CharacterLiteral.VALUE, GeneralParsers.parseLong(lines));
        data.add(CharacterLiteral.KIND, GeneralParsers.enumFromInt(CharacterKind.getEnumHelper(), lines));

        return data;
    }

    public static DataStore parseIntegerLiteralData(LineStream lines, DataStore dataStore) {
        DataStore data = parseLiteralData(lines, dataStore);

        data.add(IntegerLiteral.VALUE, new BigInteger(lines.nextLine()));

        return data;
    }

    public static DataStore parseFloatingLiteralData(LineStream lines, DataStore dataStore) {
        DataStore data = parseLiteralData(lines, dataStore);

        data.add(FloatingLiteral.VALUE, Double.parseDouble(lines.nextLine()));

        return data;
    }

    public static DataStore parseCXXBoolLiteralExprData(LineStream lines, DataStore dataStore) {
        DataStore data = parseLiteralData(lines, dataStore);

        data.add(CXXBoolLiteralExpr.VALUE, GeneralParsers.parseOneOrZero(lines.nextLine()));

        return data;
    }

}