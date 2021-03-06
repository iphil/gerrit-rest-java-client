/*
 * Copyright 2013-2014 Urs Wolfer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.urswolfer.gerrit.client.rest.http.common;

import com.google.gson.*;
import com.urswolfer.gerrit.client.rest.gson.DateDeserializer;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Date;

/**
 * @author Thomas Forrer
 */
public abstract class AbstractParserTest {
    protected JsonElement getJsonElement(String resourceName) throws Exception {
        URL url = this.getClass().getResource(resourceName);
        File file = new File(url.toURI());
        return new JsonParser().parse(new FileReader(file));
    }

    protected static Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, new DateDeserializer());
        builder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return builder.create();
    }
}
