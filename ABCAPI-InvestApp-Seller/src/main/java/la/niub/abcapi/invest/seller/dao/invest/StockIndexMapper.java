package la.niub.abcapi.invest.seller.dao.invest;

import la.niub.abcapi.invest.seller.model.StockIndexModel;

public interface StockIndexMapper {
    /**
     * deleteByPrimaryKey
     * @param id
     * @return int 
     */
    int deleteByPrimaryKey(Long id);

    /**
     * insertSelective
     * @param record
     * @return int 
     */
    int insertSelective(StockIndexModel record);

    /**
     * selectByPrimaryKey
     * @param id
     * @return stockIndexModel 
     */
    StockIndexModel selectByPrimaryKey(Long id);

    /**
     * updateByPrimaryKeySelective
     * @param record
     * @return int 
     */
    int updateByPrimaryKeySelective(StockIndexModel record);
}