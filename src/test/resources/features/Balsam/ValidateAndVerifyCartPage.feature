Feature: Validate and Verify Cart Balsam Page

  @UISmoke @Balsam @DheoClaveria @TC_1
  Scenario Outline: Validate and Verify the Add and Remove Cart Item Page
    Given DheoTester navigate to <Page> using <Role>
    When I search the <SearchItem>
    And I Select the item <ItemResultIndex> from the search result
    And I Customize and Add to Cart the item following:
      #this are the default value for the Selected Item
      | Height | Shape       | Lights                 | Setup    |
      | 12     | Sparse Tree | LED Clear Fairy Lights | Standard |
    Then I Validate And Verify the added item from Dialog
    When I Click the View Cart Button
    Then I Validate And Verify the added item from Cart Page
    And I Validate the Cart Icon should have <CartAddedCount> value
    When I Click the Remove Button
    Then I Validate and Verify that the added item is removed from the Cart Page

    Examples:
      | Page       | Role           | SearchItem     | ItemResultIndex | CartAddedCount |
      | balsamhill | ProDinnerAdmin | Christmas Tree | 3               | 1              |
      | balsamhill | ProDinnerAdmin | Christmas Tree | 1               | 1              |
      #Page variable is located in src/test/resources/serenity.conf line :107

  @FileTesting @Balsam @DheoClaveria @TC_2
  Scenario Outline: Validate and Verify the External File
    Then I Test the Excel Reader for using <fileName> fileName
    Examples:
      | fileName        |
      | userCredentials |