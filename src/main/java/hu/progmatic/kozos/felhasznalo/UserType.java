package hu.progmatic.kozos.felhasznalo;


import java.util.Arrays;

public enum UserType {
  MANAGER(
      Roles.ITEM_MODIFYING,
      Roles.USER_MODIFYING
  ),
  ASSISTANT(Roles.ITEM_MODIFYING),
  CUSTOMER(Roles.PAYING);
  private final String[] roles;


  UserType(String... roles) {
    this.roles = roles;
  }

  public String[] getRoles() {
    return roles;
  }

  public boolean hasRole(String role) {
    return Arrays.asList(roles).contains(role);
  }

  public static class Roles {
    public static final String ITEM_MODIFYING = "ITEM_MODIFYING";
    public static final String USER_MODIFYING = "USER_MODIFYING";
    public static final String PAYING = "PAYING";
  }
}
