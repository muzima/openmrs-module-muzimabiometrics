package org.javarosa.xform.parse;

import org.junit.Ignore;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.javarosa.xform.parse.ValidationMessage.Type;
import static org.javarosa.xform.parse.ValidationMessageBuilder.validationMessage;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class XFormParserTest {
    @Test
    public void validate_shouldReturnErrorIfDocumentIsNotAValidXML() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/emptyDocument.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Document has no root element!").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfHadInvalidXMLSyntax() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/invalidXMLSyntax.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("XML Syntax Error at Line: 1, Column: 18!").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfRootElementIsInvalid() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/invalidRootElement.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("XForm Parse: Unrecognized element [xforms]. Ignoring and processing children...\n" +
                "    Problem found at nodeset: /xforms\n" +
                "    With element <xforms>\n").withType(Type.ERROR).instance()));
    }

    private FileReader getFile(String file) throws FileNotFoundException {
        return new FileReader(this.getClass().getClassLoader().getResource(file).getFile());
    }

    @Test
    public void validate_shouldReturnWarningIfModelHasInvalidAttribute() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/invalidModelAttribute.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Warning: 1 Unrecognized attributes found in Element [model] and will be ignored: [id] Location:\n" +
                "\n" +
                "    Problem found at nodeset: /html/model\n" +
                "    With element <model id=\"openmrs_model\">\n").withType(Type.WARNING).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfModelHasInvalidChild() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/invalidModelChild.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Unrecognized top-level tag [invalidChildTag] found within <model>\n" +
                "    Problem found at nodeset: /html/head/model/invalidChildTag\n" +
                "    With element <invalidChildTag>\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfThereAreMultipleModels() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/multipleModels.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Multiple models not supported. Ignoring subsequent models.\n" +
                "    Problem found at nodeset: /html/head/model\n" +
                "    With element <model>\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfModelTextIsInvalid() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/invalidModelText.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Unrecognized text content found within <model>: \"invalid text content\"").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfITextHasNoTranslations() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/invalidITextNoTranslations.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("no <translation>s defined\n" +
                "    Problem found at nodeset: /html/head/model/itext\n" +
                "    With element <itext>\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnWarningIfITextInvalidAttribute() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/invalidITextAttribute.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Warning: 1 Unrecognized attributes found in Element [itext] and will be ignored: [invalid] Location:\n" +
                "\n" +
                "    Problem found at nodeset: /html/head/model/itext\n" +
                "    With element <itext invalid=\"attribute\">\n").withType(Type.WARNING).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfITextHasTranslationTagWithoutLanguageAttribute() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/translationTagWithoutLanguageAttribute.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("no language specified for <translation>\n" +
                "    Problem found at nodeset: /html/head/model/itext/translation\n" +
                "    With element <translation>\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfITextHasDuplicateTranslationTag() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/duplicateTranslationTag.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("duplicate <translation> for language 'english'\n" +
                "    Problem found at nodeset: /html/head/model/itext/translation\n" +
                "    With element <translation lang=\"english\">\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfITextHasDuplicateLanguageDefault() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/duplicateLanguageDefault.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("more than one <translation> set as default\n" +
                "    Problem found at nodeset: /html/head/model/itext/translation\n" +
                "    With element <translation lang=\"spanish\" default=\"true\">\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfTextHasNoId() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/textWithoutIdAttribute.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("no id defined for <text>\n" +
                "    Problem found at nodeset: /html/head/model/itext/translation[@lang=english][@default=true]/text\n" +
                "    With element <text>\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfTextHasInvalidChild() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/textInvalidChild.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Unrecognized element [invalid] in " +
                "Itext->translation->text").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfTextDefinitionIsDuplicate() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/duplicateTextDefinition.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("duplicate definition for text ID \"q1\" and form \"null\". Can only have one definition for each text form.\n" +
                "    Problem found at nodeset: /html/head/model/itext/translation[@lang=english][@default=true]/text\n" +
                "    With element <text id=\"q1\">\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnWarningForTextChildInvalidAttribute() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/invalidTextChildAttribute.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Warning: 1 Unrecognized attributes found in Element [value] and will be ignored: [invalid] Location:\n" +
                "\n" +
                "    Problem found at nodeset: /html/head/model/itext/translation[@lang=english][@default=true]/text[@id=q1]/value\n" +
                "    With element <value invalid=\"invalid\">\n").withType(Type.WARNING).instance()));
    }

    @Test
    public void validate_shouldReturnWarningForTextInvalidAttribute() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/invalidTextAttribute.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Warning: 1 Unrecognized attributes found in Element [text] and will be ignored: [invalid] Location:\n" +
                "\n" +
                "    Problem found at nodeset: /html/head/model/itext/translation[@lang=english][@default=true]/text\n" +
                "    With element <text id=\"q1\" invalid=\"invalid\">\n").withType(Type.WARNING).instance()));
    }

    @Test
    public void validate_shouldReturnWarningForTranslationInvalidAttribute() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/invalidTranslationAttribute.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Warning: 1 Unrecognized attributes found in Element [translation] and will be ignored: [invalid] Location:\n" +
                "\n" +
                "    Problem found at nodeset: /html/head/model/itext/translation\n" +
                "    With element <translation lang=\"english\" invalid=\"attribute\">\n").withType(Type.WARNING).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfThereAreMultipleInstanceTags() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/multipleInstance.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Multiple instances not supported. Ignoring subsequent instances.\n" +
                "    Problem found at nodeset: /html/head/model/instance\n" +
                "    With element <instance>\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfInstanceHadMoreThanOneChild() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/multipleChildInAnInstance.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("XForm Parse: <instance> has more than one child element\n" +
                "    Problem found at nodeset: /html/head/model/instance\n" +
                "    With element <instance>\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnWarningIfBindHasInvalidAttributes() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/invalidBindAttribute.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Warning: 1 Unrecognized attributes found in Element [bind] and will be ignored: [invalid] Location:\n" +
                "\n" +
                "    Problem found at nodeset: /html/head/model/bind\n" +
                "    With element <bind nodeset=\"name\" invalid=\"attribute\"/>\n").withType(Type.WARNING).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfBindHasNoNodeSet() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/bindWithNoNodeset.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("XForm Parse: <bind> without nodeset\n" +
                "    Problem found at nodeset: /html/head/model/bind\n" +
                "    With element <bind/>\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfBindNodeSetHasAnInvalidXPath() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/bindWithInvalidXPathForNodeSet.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Parse error in XPath path: []\n" +
                "    Problem found at nodeset: /html/head/model/bind\n" +
                "    With element <bind nodeset=\"\"/>\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnWarningIfBindNodesetHasAnInvalidXPathExpression() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/bindWithNoMatchingInstance.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("WARNING: Bind [/person/children/child/start] " +
                "matches no nodes; ignoring bind...").withType(Type.WARNING).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfBindExistsWithoutAnInstance() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/bindWithNoInstance.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("No model instance available to do bind\n" +
                "    Problem found at nodeset: /html/head/model/bind\n" +
                "    With element <bind nodeset=\"children/child/start\"/>\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfBindHasInvalidRequiredCondition() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/bindWithInvalidRequiredCondition.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Invalid XPath expression []! at required \n" +
                "    Problem found at nodeset: /html/head/model/bind\n" +
                "    With element <bind nodeset=\"name\" required=\"\"/>\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfBindHasInvalidReadOnlyCondition() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/bindWithInvalidReadOnlyCondition.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Invalid XPath expression []! at readonly \n" +
                "    Problem found at nodeset: /html/head/model/bind\n" +
                "    With element <bind nodeset=\"name\" readonly=\"\"/>\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfBindHasInvalidConstraintCondition() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/bindWithInvalidConstraintCondition.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Invalid XPath expression []! at constraint\n" +
                "    Problem found at nodeset: /html/head/model/bind\n" +
                "    With element <bind nodeset=\"name\" constraint=\"\"/>\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfBindHasInvalidCalculateCondition() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/bindWithInvalidCalculateCondition.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("Invalid XPath expression []! at calculate \n" +
                "    Problem found at nodeset: /html/head/model/bind\n" +
                "    With element <bind nodeset=\"name\" calculate=\"\"/>\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfBindingIDIsNotUnique() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/bindingIdNotUnique.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("XForm Parse: <bind>s with duplicate ID: 'name'").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfSubmissionHasInvalidBind() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/submissionWithInvalidBind.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("XForm Parse: invalid binding ID in submit' invalid'\n" +
                "    Problem found at nodeset: /html/head/model/submission\n" +
                "    With element <submission bind=\"invalid\">\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfSubmissionHasInvalidRef() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/submissionWithInvalidRef.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("java.lang.RuntimeException: Parse error in XPath path: [] at ref \n" +
                "    Problem found at nodeset: /html/head/model/submission\n" +
                "    With element <submission ref=\"\">\n").withType(Type.ERROR).instance()));
    }

    @Test
    public void validate_shouldReturnErrorIfControlHasInvalidBinding() throws Exception {
        XFormParser parser = new XFormParser(getFile("javarosa/invalidControlBinding.xml"));
        ValidationMessages messages = parser.validate();
        assertThat(messages.getList(), hasItem(validationMessage().withMessage("XForm Parse: invalid binding ID ''\n" +
                "    Problem found at nodeset: /html/head/input\n" +
                "    With element <input bind=\"\"/>\n").withType(Type.ERROR).instance()));
    }


}
