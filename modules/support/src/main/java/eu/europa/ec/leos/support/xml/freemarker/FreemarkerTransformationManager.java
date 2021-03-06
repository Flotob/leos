/**
 * Copyright 2015 European Commission
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *     https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package eu.europa.ec.leos.support.xml.freemarker;

import com.google.common.base.Stopwatch;
import eu.europa.ec.leos.model.content.LeosDocument;
import eu.europa.ec.leos.support.xml.TransformationManager;
import freemarker.ext.dom.NodeModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class FreemarkerTransformationManager implements TransformationManager {

    private static final Logger LOG = LoggerFactory.getLogger(FreemarkerTransformationManager.class);

    @Autowired
    private Configuration freemarkerConfiguration;

    @Value("${leos.freemarker.ftl.documentView}")
    private String editableXHtmlTemplate;

    @Value("${leos.freemarker.ftl.htmlPreview}")
    private String readOnlyHtmlTemplate;

    /** This method will return XML along with wrappers to invoke CKEditor */
    @Override
    public String toEditableXml(final LeosDocument document,String contextPath ){
        return transform(document, editableXHtmlTemplate, contextPath);
    }

    /** This method will return XML along with CSS to display the content of the document on the browser */
    @Override
    public String toReadOnlyHtml(final LeosDocument document,String contextPath) {
        return transform(document, readOnlyHtmlTemplate, contextPath);
    }

    private String transform(LeosDocument leosDocument, String templateName ,String  contextPath) {
        LOG.trace("Transforming document using {} template...", templateName);
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            StringWriter outputWriter = new StringWriter();
            Template template = freemarkerConfiguration.getTemplate(templateName);
            
            NodeModel nodeModel = XmlNodeModelHandler.parseXmlStream(leosDocument.getContentStream());

            Map root = new HashMap<String, Object>();
            Map headers =new HashMap<String, String>();
            
            headers.put("contextPath", contextPath);
            
            root.put("xml_data", nodeModel);
            root.put("headers", headers);
            
            template.process(root, outputWriter);
            return outputWriter.getBuffer().toString();
        } catch (Exception ex) {
            LOG.error("Transformation error!", ex);
            throw new RuntimeException(ex);
        } finally {
            stopwatch.stop();
            LOG.trace("Transformation finished! ({} milliseconds)", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }
    }
}
