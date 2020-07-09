package be.chaixdev.streamarksbackend.rest.note.validators;

import be.chaixdev.streamarksbackend.model.Note;
import be.chaixdev.streamarksbackend.rest.common.ValidationError;
import be.chaixdev.streamarksbackend.rest.common.Validator;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AppendNoteValidator extends Validator<Note> {



    @Override
    public List<ValidationError> validate(Note toValidate) {
        List<ValidationError> errors = new ArrayList<>();
        // note must contain a valid interval
        if(toValidate.getInterval()==null){
            errors.add(new ValidationError("$.interval", "missing property interval "));
        } else if(StringUtils.isBlank(toValidate.getInterval().getStart())){
            errors.add(new ValidationError("$.interval", "missing start on interval"));
        }

        return errors;
    }
}
