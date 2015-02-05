package org.javarosa.xform.parse;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.javarosa.xform.parse.ValidationMessage.Type;
import static org.junit.Assert.assertThat;


public class ValidationMessageTest {
    @Test
    public void shouldConfirmThatTwoValidationMessagesAreEqualIfTheMessagesAndTheTypesAreTheSame() throws Exception {
        assertThat(new ValidationMessage("message", Type.WARNING), is(equalTo(new ValidationMessage("message", Type.WARNING))));
    }
}
