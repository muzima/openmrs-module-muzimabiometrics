package org.javarosa.xform.parse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationMessages implements Serializable {
    private List<ValidationMessage> list = new ArrayList<ValidationMessage>();

    public void addWarning(String warning) {
        list.add(new ValidationMessage(warning, ValidationMessage.Type.WARNING));
    }

    public void clear() {
        list.clear();
    }

    public void addError(String error) {
        list.add(new ValidationMessage(error, ValidationMessage.Type.ERROR));
    }

    public List<ValidationMessage> getList() {
        return Collections.unmodifiableList(list);
    }

}
