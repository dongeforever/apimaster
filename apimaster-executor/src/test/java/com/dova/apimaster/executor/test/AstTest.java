package com.dova.apimaster.executor.test;

import com.dova.apimaster.common.util.JSON;
import com.dova.apimaster.executor.ast.domain.AstNode;
import com.dova.apimaster.executor.ast.helper.CharHelper;
import com.dova.apimaster.executor.ast.impl.AstExecutor;
import com.dova.apimaster.executor.ast.impl.AstParser;
import org.junit.Test;

/**
 * Created by liuzhendong on 16/5/27.
 */
public class AstTest {
    AstParser astParser = new AstParser();
    AstExecutor astExecutor = new AstExecutor();
    @Test
    public void parseNumricExpression()throws Exception{
        System.out.println(CharHelper.getOpStart());
        AstNode root = astParser.parse("1 + 1 + 2.1  == 4.11");
        astExecutor.execute(root);
        System.out.println(JSON.toJson(root));
        System.out.println(root.get());
    }
}
