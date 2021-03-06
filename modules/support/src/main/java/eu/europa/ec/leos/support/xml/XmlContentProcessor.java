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
package eu.europa.ec.leos.support.xml;

import eu.europa.ec.leos.vo.TableOfContentItemVO;

import java.util.List;

public interface XmlContentProcessor {

    /**
     * 
     * @param xmlContent
     * @param tagName
     * @param idAttributeValue 
     * @return the element with the tagname and the id, or the first element with the tagname if id is null
     */
    public String getElementByNameAndId(byte[] xmlContent, String tagName, String idAttributeValue);

    public byte[] replaceElementByTagNameAndId(byte[] xmlContent, String newContent, String tagName, String idAttributeValue);

    public byte[] deleteElementByTagNameAndId(byte[] xmlContent, String tagName, String idAttributeValue);

    public byte[] insertElementByTagNameAndId(byte[] xmlContent, String articleTemplate, String tagName, String idAttributeValue, boolean before);

    public List<TableOfContentItemVO> buildTableOfContent(byte[] xmlContent);

    public byte[] createDocumentContentWithNewTocList(List<TableOfContentItemVO> tableOfContentItemVOs, byte[] contentStream);

    public byte[] replaceElementWithTagName(byte[] xmlContent, String tagName, String newContent);

    public byte[] appendElementToTag(byte[] xmlContent, String tagName, String newContent);

    public byte[] renumberArticles(byte[] xmlContent, String language);
    
    public byte[] injectTagIdsinXML(byte[] xmlContent) ;
    
    public byte[] doXMLPostProcessing(byte[] xmlContent) ;

}
