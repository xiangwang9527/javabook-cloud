package cn.javabook.chapter08.iterator;

/**
 * 购物车接口
 *
 */
public interface Cart {
	/**
	 * 增加
	 *
	 * @param product
	 */
	public void add(Product product);

	/**
	 * 删除
	 *
	 * @param product
	 */
	public void remove(Product product);

	/**
	 * 抽检
	 *
	 * @return
	 */
	public Iterator createIterator();
}
