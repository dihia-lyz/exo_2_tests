@GetAll
Feature: Cette fontionnalite permet de recuperer toutes les annonces

  Scenario :  client makes call to GET /annonce/all
    When : client calls /annonce/all
    Then the client receives status code of 200
    And the client receives the annonces list

@Update
Feature: Cette fontionnalite permet de modifier une annonce

  Scenario :  client makes call to PUT /annonce/
    When : client calls /annonce/all
    Then the client receives status code of 200
    And the client receives the modified annonce

