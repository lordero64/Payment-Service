package com.iprody.payment.service.app.services;

import jakarta.validation.constraints.NotNull;

public class NoteUpdateDto {
    @NotNull
    private String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
