package tn.esprit.Books_Clubs.Services.IServices;

import tn.esprit.Books_Clubs.entities.*;

public interface IStockService {

    Stock getStockByBookId(Long bookId);

    Stock updateStock(Long bookId, int quantity);

    Stock addQuantity(Long bookId, int qte);

    Stock removeQuantity(Long bookId, int qte);
}

