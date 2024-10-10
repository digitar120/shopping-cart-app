# Descripción de la app
Feature: Shopping cart REST API
  Scenario: The user wins when they choose Rock and the computer chooses Scissors
    Given the user will choose "rock"
    And the computer will choose "scissors"
    When they play
    Then verify that the computer chose "scissors"
    And the user wins