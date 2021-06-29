package domain.validator;

import domain.Reservation;

public class ReservationValidator implements Validator<Reservation>{
    @Override
    public void validate(Reservation entity) throws ValidationException {

        if(!entity.getClientName().matches("^[\\p{L} .'-]+$"))
            throw new ValidationException("Invalid client name");

        if(!(entity.getClientPhone().length()==10) || !entity.getClientPhone().matches("[0-9]+"))
            throw new ValidationException("Invalid phone number");

    }
}
