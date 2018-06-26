/*
 * cmcdemo
 * Copyright (C) 2018.  e2x.io
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.e2x.utils;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {
    public static String object2Json(Object o){
        String jsonStr="";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonGenerator jsonGenerator = objectMapper.getJsonFactory()
                    .createJsonGenerator(System.out, JsonEncoding.UTF8);
            jsonStr = objectMapper.writeValueAsString(o);
        }catch (IOException e){
            e.printStackTrace();
        }
        return jsonStr;
    }
}
