/*
 * @author Austin Wong
 * email: austinwongdev@gmail.com
 * date: Aug 26, 2021
 * purpose: 
 */

package com.hs.superherosightings.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error {

    private String message;
    private boolean old;

    public Error(String message) {
        this.message = message;
    }

}
