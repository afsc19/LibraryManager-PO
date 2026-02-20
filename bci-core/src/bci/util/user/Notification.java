package bci.util.user;

import java.io.Serializable;

public abstract class Notification implements Serializable {
   private String _message;

   protected Notification(String message) {
      _message = message;
   }

   public String getMessage() {
      return _message;
   };


}
