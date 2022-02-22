package hu.progmatic.kozos.felhasznalo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FelhasznaloServiceTest {
  @Autowired
  private FelhasznaloService felhasznaloService;

  @Test
  @DisplayName("Összes felhasználó listázása")
  @WithMockUser(roles = UserType.Roles.USER_MODIFYING)
  void findAll() {
    assertFelhasznaloLetezik("admin");
  }

  @Test
  @DisplayName("Felhasználó hozzáadása")
  @WithMockUser(roles = {UserType.Roles.ITEM_MODIFYING, UserType.Roles.USER_MODIFYING})
  void add() {
    UjFelhasznaloCommand command = new UjFelhasznaloCommand("ujtesztfelhasznalo", "x", UserType.MANAGER, "1111");
    felhasznaloService.add(command);
    assertFelhasznaloLetezik("ujtesztfelhasznalo");
  }

  @Test
  @DisplayName("Felhasználó létezik hibaüzenet")
  @WithMockUser(roles = UserType.Roles.ITEM_MODIFYING)
  void felhasznaloLetezikHiba() {
    UjFelhasznaloCommand command = new UjFelhasznaloCommand("admin", "x", UserType.MANAGER, "11111");
    FelhasznaloLetrehozasException e = null;
    try {
      felhasznaloService.add(command);
    } catch (FelhasznaloLetrehozasException ex) {
      e = ex;
    }
    assertThat(e)
        .isNotNull()
        .extracting(FelhasznaloLetrehozasException::getMessage)
        .isEqualTo("Ilyen névvel már létezik felhasználó!");
  }

  @Test
  @DisplayName("Felhasználó törlése")
  @WithMockUser(roles = {UserType.Roles.ITEM_MODIFYING, UserType.Roles.USER_MODIFYING})
  void torles() {
    String tesztFelhasznaloNev = "ujtesztfelhasznalotorleshez";
    UjFelhasznaloCommand command = new UjFelhasznaloCommand(tesztFelhasznaloNev, "x", UserType.MANAGER, "11111");
    felhasznaloService.add(command);
    Optional<Felhasznalo> elmentett = felhasznaloService.findByName(tesztFelhasznaloNev);
    assertThat(elmentett).isPresent();
    felhasznaloService.delete(elmentett.get().getId());
    assertThat(felhasznaloService.findByName(tesztFelhasznaloNev)).isEmpty();
  }

  @Test
  @DisplayName("Felhasználó jogosultságok lekérése - user")
  @WithUserDetails(userDetailsServiceBeanName = "myUserDetailsService")
  void userHasRole() {
    assertTrue(felhasznaloService.hasRole(UserType.Roles.USER_MODIFYING));
    assertFalse(felhasznaloService.hasRole(UserType.Roles.ITEM_MODIFYING));
  }

  @Test
  @DisplayName("Felhasználó jogosultságok lekérése - admin")
  @WithUserDetails(value = "admin", userDetailsServiceBeanName = "myUserDetailsService")
  void adminHasRole() {
    assertTrue(felhasznaloService.hasRole(UserType.Roles.USER_MODIFYING));
    assertTrue(felhasznaloService.hasRole(UserType.Roles.ITEM_MODIFYING));
  }

  @Test
  @DisplayName("Felhasználó jogosultságok lekérése - guest")
  @WithUserDetails(value = "guest", userDetailsServiceBeanName = "myUserDetailsService")
  void guestHasRole() {
    assertFalse(felhasznaloService.hasRole(UserType.Roles.USER_MODIFYING));
    assertFalse(felhasznaloService.hasRole(UserType.Roles.ITEM_MODIFYING));
  }

  @Test
  @DisplayName("Felhasználó id lekérése")
  @WithUserDetails(value = "guest", userDetailsServiceBeanName = "myUserDetailsService")
  void userId() {
    Long felhasznaloId = felhasznaloService.getFelhasznaloId();
    assertThat(felhasznaloId)
        .isNotNull()
        .isPositive();
  }

  private void assertFelhasznaloLetezik(String felhasznalonev) {
    assertThat(felhasznaloService.findAll())
        .isNotEmpty()
        .extracting(Felhasznalo::getNev)
        .contains(felhasznalonev);
  }
}