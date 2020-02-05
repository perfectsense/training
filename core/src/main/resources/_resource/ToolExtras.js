require(['jquery', 'bsp-utils'], function($, bsp_utils) {

  bsp_utils.onDomInsert(document, '[data-hidden-mods]', {
    'insert': function (el) {

      var $el = $(el),
        fieldNames = JSON.parse($el.attr('data-hidden-mods'))['fieldNames'],
        $objectInputs = $el.closest('.objectInputs'),
        $tabsWrapper = $objectInputs.find('.tabs-wrapper'),
        $selectedTab = $tabsWrapper.find('.state-selected[data-tab]'),
        $allFields = $objectInputs.find('[data-field-name]');

      if (fieldNames.length === 0) {
        $allFields.show();
      }

      var selector = "[data-field-name=\"" + fieldNames.join("\"], [data-field-name=\"") + "\"]";

      var $fieldsToHide = $objectInputs.find(selector);

      $fieldsToHide.toggleClass('conditional-mod-hidden', true);

      $allFields.not($fieldsToHide).toggleClass('conditional-mod-hidden', false);

      $tabsWrapper.find('[data-tab]').each(function() {

        var $tab = $(this),
          tabName = $tab.attr('data-tab'),
          $tabFields = $objectInputs.find('.inputContainer[data-tab="' + tabName + '"]'),
          $hiddenTabFields = $tabFields.filter('.conditional-mod-hidden');

        $tab.toggleClass('conditional-mod-hidden', $tabFields.size() === $hiddenTabFields.size());
      });

      if ($selectedTab.hasClass('conditional-mod-hidden')) {
        $objectInputs.find('[data-tab="Main"]').click();
      }
    }
  });

  if (window.ADD_TO_TOP_FIELDS !== undefined && window.ADD_TO_TOP_FIELDS.length > 0) {

    var i, selectorParts = [ ];
    for (i = 0; i < window.ADD_TO_TOP_FIELDS.length; i += 1) {

      selectorParts.push('[data-type="' + ADD_TO_TOP_FIELDS[i]['class'] + '"] [data-field-name="' + window.ADD_TO_TOP_FIELDS[i]['field'] + '"]');
    }

    var ADD_BUTTON_SELECTOR = selectorParts.join(' .repeatableForm > .addButtonContainer, ') + ' .repeatableForm > .addButtonContainer';
    var LIST_ITEM_SELECTOR = selectorParts.join(' > .repeatableForm > ol > li, ') + ' > .repeatableForm > ol > li';

    // on insert of the .addButtonContainer element, insert it before the ordered list containing the repeatable input
    bsp_utils.onDomInsert(document, ADD_BUTTON_SELECTOR, {
      'insert': function (el) {

        var $el = $(el),
          $ol = $el.prev('ol');

        if ($ol.size() === 0) {
          return;
        }

        // add a styling class to reverse the margin styling
        $el.closest(".inputContainer").toggleClass('repeatableAddToTop');

        // insert the add button container before the ordered list
        $ol.before($el);
      }
    });

    var $observedItems = $();

    // on insert of a new list item, hoist it to the top of the list
    bsp_utils.onDomInsert(document, LIST_ITEM_SELECTOR, {
      'insert': function (el) {

        // filter list item to exclude items already observed
        var $item = $(el).not($observedItems),
          $list = $item.closest('ol');

        // presence of the 'data-label-html' attribute is the only way to distinguish
        // newly-added items from existing ones on page init
        if ($item.size() === 0 || $item.attr('data-label-html') !== undefined) {
          return;
        }

        // accumulate the list item in the set of observed items
        $observedItems = $observedItems.add($item);

        // move the new list item to the beginning of the list
        $list.prepend($item);
      }
    });
  }
});
