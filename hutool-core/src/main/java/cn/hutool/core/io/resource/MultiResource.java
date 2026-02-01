package cn.hutool.core.io.resource;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IORuntimeException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/**
 * 多资源组合资源<br>
 * 此资源为一个利用游标自循环资源，只有调用{@link #next()} 方法才会获取下一个资源，使用完毕后调用{@link #reset()}方法重置游标
 *
 * @author looly
 * @since 4.1.0
 */
public class MultiResource implements Resource, Iterable<Resource>, Iterator<Resource>, Serializable {
	private static final long serialVersionUID = 1L;

	private final List<Resource> resources;
	/**
	 * 游标
	 */
	private int cursor = -1;

	/**
	 * 构造
	 *
	 * @param resources 资源数组
	 */
	public MultiResource(Resource... resources) {
		this(CollUtil.newArrayList(resources));
	}

	/**
	 * 构造
	 *
	 * @param resources 资源列表
	 */
	public MultiResource(Collection<Resource> resources) {
		if(resources instanceof List) {
			this.resources = (List<Resource>)resources;
		}else {
			this.resources = CollUtil.newArrayList(resources);
		}
	}

	@Override
	public String getName() {
		return resources.get(getValidCursor()).getName();
	}

	@Override
	public URL getUrl() {
		return resources.get(getValidCursor()).getUrl();
	}

	@Override
	public InputStream getStream() {
		return resources.get(getValidCursor()).getStream();
	}

	@Override
	public boolean isModified() {
		return resources.get(getValidCursor()).isModified();
	}

	@Override
	public BufferedReader getReader(Charset charset) {
		return resources.get(getValidCursor()).getReader(charset);
	}

	@Override
	public String readStr(Charset charset) throws IORuntimeException {
		return resources.get(getValidCursor()).readStr(charset);
	}

	@Override
	public String readUtf8Str() throws IORuntimeException {
		return resources.get(getValidCursor()).readUtf8Str();
	}

	@Override
	public byte[] readBytes() throws IORuntimeException {
		return resources.get(getValidCursor()).readBytes();
	}

	@Override
	public Iterator<Resource> iterator() {
		return resources.iterator();
	}

	@Override
	public boolean hasNext() {
		return getValidCursor() < resources.size();
	}

	@Override
	public synchronized Resource next() {
		if (!hasNext()) {
			throw new ConcurrentModificationException();
		}
		this.cursor++;
		return this;
	}

	@Override
	public void remove() {
		this.resources.remove(getValidCursor());
	}

	/**
	 * 重置游标
	 */
	public synchronized void reset() {
		this.cursor = -1;
	}

	/**
	 * 增加资源
	 * @param resource 资源
	 * @return this
	 */
	public MultiResource add(Resource resource) {
		this.resources.add(resource);
		return this;
	}

	/**
	 * 获取当前有效游标位置的资源
	 *
	 * @return 资源
	 */
	private int getValidCursor() {
		return Math.max(cursor, 0);
	}
}
