package com.dova.apimaster.executor.test;

import com.dova.apimaster.common.util.JSON;
import com.dova.apimaster.executor.ast.domain.AstNode;
import com.dova.apimaster.executor.ast.helper.CharHelper;
import com.dova.apimaster.executor.ast.impl.AstParser;
import org.junit.Test;

/**
 * Created by liuzhendong on 16/5/27.
 */
public class AstParserTest {
    AstParser astParser = new AstParser();
    @Test
    public void parseNumricExpression()throws Exception{
        System.out.println(CharHelper.getOpStart());
        AstNode root = astParser.parse("1+2*3+1");
        System.out.println(JSON.toJson(root));
    }
}
