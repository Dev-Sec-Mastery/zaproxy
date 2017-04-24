/*
 * Zed Attack Proxy (ZAP) and its related class files.
 * 
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package org.zaproxy.zap.spider.filters;

import org.apache.log4j.Logger;
import org.parosproxy.paros.network.HttpMessage;

/**
 * A ParseFilter is used to filter which resources should be parsed by the Spider after they have
 * already been fetched and which shouldn't.
 */
public abstract class ParseFilter {

	/** The Constant log. */
	protected static final Logger log = Logger.getLogger(ParseFilter.class);

	/**
	 * Checks if the resource must be ignored and not processed.
	 * 
	 * @param responseMessage the response message after the resource was fetched
	 * @return true, if is filtered
	 * @deprecated (TODO add version) Use {@link #filtered(HttpMessage)} instead, which allows to provide the reason why the
	 *             message was filtered.
	 */
	@Deprecated
	public boolean isFiltered(HttpMessage responseMessage) {
		return filtered(responseMessage).isFiltered();
	}

	/**
	 * Tells whether or not the given resource is filtered. Filtered resources are not parsed.
	 * <p>
	 * Default is not filtered.
	 *
	 * @param responseMessage the HTTP message containing the response to be or not parsed.
	 * @return the filter result, must not be {@code null}.
	 * @since TODO add version
	 */
	public FilterResult filtered(HttpMessage responseMessage) {
		return FilterResult.NOT_FILTERED;
	}

	/**
	 * The result of a {@link ParseFilter}'s check.
	 * <p>
	 * Used to indicate if a resource was filtered and why.
	 *
	 * @since TODO add version
	 * @see #NOT_FILTERED
	 * @see #ParseFilter(String)
	 */
	public static final class FilterResult {

		/**
		 * Indicates that the resource was not filtered.
		 */
		public static final FilterResult NOT_FILTERED = new FilterResult();

		/**
		 * Indicates that the resource was filtered, with no specific reason.
		 */
		public static final FilterResult FILTERED = new FilterResult("");

		private final boolean filtered;
		private final String reason;

		/**
		 * Constructs a {@code FilterResult}, not filtered and with empty reason.
		 */
		private FilterResult() {
			this(false, "");
		}

		/**
		 * Constructs a {@code FilterResult} with the reason why the resource will not be parsed.
		 *
		 * @param reason the reason why the resource was filtered.
		 * @see #NOT_FILTERED
		 */
		public FilterResult(String reason) {
			this(true, reason);
		}

		/**
		 * Constructs a {@code FilterResult} with the given filtered state and reason.
		 *
		 * @param filtered {@code true} if the resource was filtered, {@code false} otherwise.
		 * @param reason the reason why the resource was filtered.
		 * @throws IllegalArgumentException if the given {@code reason} is {@code null}.
		 */
		private FilterResult(boolean filtered, String reason) {
			if (reason == null) {
				throw new IllegalArgumentException("Parameter reason must not be null.");
			}
			this.filtered = filtered;
			this.reason = reason;
		}

		/**
		 * Tells whether or not the resource was filtered.
		 * <p>
		 * Filtered resources are not parsed.
		 *
		 * @return {@code true} if the resource was filtered, {@code false} otherwise.
		 * @see #getReason()
		 */
		public boolean isFiltered() {
			return filtered;
		}

		/**
		 * Gets the reason why the resource was filtered.
		 *
		 * @return the reason why the resource was filtered, never {@code null}.
		 * @see #isFiltered()
		 */
		public String getReason() {
			return reason;
		}
	}
}
