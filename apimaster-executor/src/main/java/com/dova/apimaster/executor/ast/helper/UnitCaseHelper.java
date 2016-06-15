package com.dova.apimaster.executor.ast.helper;

import com.dova.apimaster.common.domain.UnitCase;
import com.dova.apimaster.common.util.JSON;
import com.dova.apimaster.executor.ast.domain.Keyword;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by liuzhendong on 16/5/27.
 */
public class UnitCaseHelper {

   public static ObjectNode createRequestNode(UnitCase unitCase){
       JsonNode path = unitCase.getPathVariables() == null ? JSON.newObjectNode() : unitCase.getPathVariables();
       JsonNode header = unitCase.getHeaders() == null ? JSON.newObjectNode() : unitCase.getHeaders();
       JsonNode body = unitCase.getRequestBody() == null ? JSON.newObjectNode() : unitCase.getRequestBody();
       ObjectNode request = JSON.newObjectNode();
       request.set(Keyword.PATH, path);
       request.set(Keyword.HEADER, header);
       request.set(Keyword.BODY, body);
       return request;
   }
}
