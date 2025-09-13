Feature: Beymen Tests


  @TESTINIUM
  Scenario: 1) Beymen
    * Open home page
    * Click "COOKIES_KABUL_BUTTON"
    * Click "POPUP_KAPAT_BUTTON"
    * Check "HESABIM" element is visible
    * Enter "0","1" search term in "SEARCH_BAR"
    * Click "SEARCH_BAR_SIL_BUTTON"
    * Enter "0","2" search term in "SEARCH_BAR_FOCUS"
    * Press enter
    * Select random product
    * Save product information to file
    * Save product price
    * Select random size
    * Click "SEPETE_EKLE_BUTTON"
    * Click "SEPETIM_BUTTON"
    * Assert prices match
    * Click "SEPET_ADET_BOX"
    * Click "SEPET_ADET_BOX_2"
    * Assert quantity value is "2"
    * Click "SEPET_SIL_BUTTON"
    * Assert "SEPET_URUN_1" element is not visible
    * Assert "SEPET_MESSAGE_URUN_BULUNMAMAKTADIR" element is visible

   