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

import org.apache.openjpa.enhance.PCEnhancer
import org.apache.openjpa.lib.util.Options
import org.gradle.api.Plugin
import org.gradle.api.Project

class OpenJpaPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        target.extensions.create("openjpa", OpenJpaExtension)
        def task = target.tasks.create("openjpaEnhance").doLast {
            URL[] urls = collectURLs(target)
            def oldClassLoader = Thread.currentThread().getContextClassLoader()
            def loader = new URLClassLoader(urls, oldClassLoader)
            try {
                Thread.currentThread().setContextClassLoader(loader)
                PCEnhancer.run(
                        target.fileTree(target.sourceSets.main.output.classesDir).files as String[],
                        new Options(target.openjpa.toProperties())
                )
            } finally {
                Thread.currentThread().setContextClassLoader(oldClassLoader)
            }
        }
        target.tasks.add task
        target.tasks["classes"].dependsOn task
    }

    private static URL[] collectURLs(target) {
        def compileClassPathURLs = target.configurations["compile"].files.collect {
            it.toURI().toURL();
        }
        def resourceUrls = target.sourceSets.main.resources.srcDirs.collect {
            return target.file(it).toURI().toURL()
        }
        def classesDir = target.sourceSets.main.output.classesDir
        return compileClassPathURLs + resourceUrls + classesDir.toURI().toURL()
    }

}
