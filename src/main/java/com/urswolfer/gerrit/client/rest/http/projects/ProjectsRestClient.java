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

package com.urswolfer.gerrit.client.rest.http.projects;

import com.google.common.base.Strings;
import com.google.gerrit.extensions.api.projects.ProjectApi;
import com.google.gerrit.extensions.api.projects.Projects;
import com.google.gerrit.extensions.common.ProjectInfo;
import com.google.gerrit.extensions.restapi.RestApiException;
import com.google.gson.JsonElement;
import com.urswolfer.gerrit.client.rest.http.GerritRestClient;
import com.urswolfer.gerrit.client.rest.http.util.UrlUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author Urs Wolfer
 */
public class ProjectsRestClient extends Projects.NotImplemented implements Projects {

    private final GerritRestClient gerritRestClient;
    private final ProjectsParser projectsParser;

    public ProjectsRestClient(GerritRestClient gerritRestClient,
                              ProjectsParser projectsParser) {
        this.gerritRestClient = gerritRestClient;
        this.projectsParser = projectsParser;
    }

    @Override
    public ListRequest list() {
        return new ListRequest() {
            @Override
            public List<ProjectInfo> get() throws RestApiException {
                return ProjectsRestClient.this.list(this);
            }
        };
    }

    @Override
    public ProjectApi name(String name) throws RestApiException {
        return new ProjectApiRestClient(gerritRestClient, projectsParser, name);
    }

    private List<ProjectInfo> list(ListRequest listParameter) throws RestApiException {
        String query = "";

        if (listParameter.getDescription()) {
            query = UrlUtils.appendToUrlQuery(query, "d");
        }
        if (!Strings.isNullOrEmpty(listParameter.getPrefix())) {
            query = UrlUtils.appendToUrlQuery(query, "p=" + listParameter.getPrefix());
        }
        if (listParameter.getLimit() > 0) {
            query = UrlUtils.appendToUrlQuery(query, "n=" + listParameter.getLimit());
        }
        if (listParameter.getStart() > 0) {
            query = UrlUtils.appendToUrlQuery(query, "S=" + listParameter.getStart());
        }

        String url = "/projects/";
        if (!Strings.isNullOrEmpty(query)) {
            url += '?' + query;
        }

        JsonElement result = gerritRestClient.getRequest(url);
        if (result == null) {
            return Collections.emptyList();
        }
        return projectsParser.parseProjectInfos(result);
    }
}
