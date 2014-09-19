/*
 * (C) Copyright 2014 SCHMUTTERER+PARTNER IT GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package at.schmutterer.oss.gradle.openjpa

class OpenJpaExtension {

    Boolean addDefaultConstructor = true
    String connectionDriverName
    Map<String,String> connectionProperties
    Boolean enforcePropertyRestrictions = false
    String[] excludes
    String[] includes
    File persistenceXmlFile
    Map toolProperties = [:]

    Properties toProperties() {
        def result = new Properties()
        result.put("addDefaultConstructor", addDefaultConstructor)
        if (connectionDriverName) {
            result.put("connectionDriverName", connectionDriverName)
        }
        if (connectionProperties) {
            String propertiesAsString = connectionProperties.collect  {key, value ->
                "$key=$value"
            }.join(",")
            result.put("connectionProperties", propertiesAsString)
        }
        result.put("enforcePropertyRestrictions", enforcePropertyRestrictions)
        if (excludes){
            result.put("excludes", excludes.join(","))
        }
        if (includes){
            result.put("includes", includes.join(","))
        }
        if (persistenceXmlFile) {
            result.put("persistenceXmlFile", persistenceXmlFile.absolutePath)
        }
        result.put("toolProperties", toolProperties)
        return result;
    }

}
