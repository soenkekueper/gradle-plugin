/*
 * The MIT License
 *
 * Copyright (c) 2017, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.gradle;

import hudson.model.Label;
import hudson.tools.ToolDescriptor;
import hudson.tools.ToolInstallation;
import hudson.tools.ToolProperty;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.util.Collections;
import java.util.List;

/**
 * @author Alex Johnson
 */
public class WithGradleWorkFlowTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void testGradleWorkflowStep() throws Exception {

        WorkflowJob p1 = j.jenkins.createProject(WorkflowJob.class, "FakeProject");
        GradleInstallation g = new GradleInstallation("g2", "/no/such/home", Collections.EMPTY_LIST);
        ToolInstallation.all().add((ToolDescriptor) g.getDescriptor());

        p1.setDefinition(new CpsFlowDefinition("node {\n" +
                "writeFile(file:'build.gradle', text:'defaultTasks \\\'hello\\\'\\ntask hello << { println \\\'Hello\\\' }') \n" +
                    "withGradle (gradle: 'g2') {\n" +
                        "sh 'echo BODY CALLED'\n" +
                    "}\n" +
                "}", false));
        j.assertBuildStatusSuccess(p1.scheduleBuild2(0));
    }
}