require(['jquery', 'bsp-utils'], function($, bsp_utils) {

  bsp_utils.onDomInsert(document, '.objectInputs', {
    'insert': function (el) {
      if ($('.objectInputs .objectInputs').index(el) === -1) {

        var $objectInputs = $(el),
          $contentFormMain = $objectInputs.closest('.contentForm-main'),
          $editTopContainer = $objectInputs.find('.bsp-edit-top-html');

        if ($contentFormMain.size() === 1 && $editTopContainer.size() === 0) {
          $editTopContainer = $('<div />', {
            'class': 'bsp-edit-top-html',
            'data-dynamic-html': '${content.as["brightspot.core.tool.EditTopHtmlContainerModification"].getContainerHtml(pageContext)}'
          });
          $objectInputs.before($editTopContainer);
        }
      }
    }
  });
});
