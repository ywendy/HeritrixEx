/* FrontierScheduler
 * 
 * $Id: FrontierScheduler.java 4671 2006-09-26 23:47:15Z paul_jack $
 *
 * Created on June 6, 2005
 * 
 * Copyright (C) 2005 Internet Archive.
 *
 * This file is part of the Heritrix web crawler (crawler.archive.org).
 *
 * Heritrix is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * any later version.
 *
 * Heritrix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with Heritrix; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package org.archive.crawler.postprocessor;


import org.archive.crawler.StartHeritrix;
import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.datamodel.FetchStatusCodes;
import org.archive.crawler.datamodel.SysConfig;
import org.archive.crawler.db.SeedsService;
import org.archive.crawler.extractor.ExtractorHTML;
import org.archive.crawler.framework.Processor;
import org.archive.crawler.util.Toolkit;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.archive.crawler.db.DataService.countLikeUrl;
import static org.archive.crawler.db.DataService.isUrlExist;


/**
 * 'Schedule' with the Frontier CandidateURIs being carried by the passed
 * CrawlURI.
 * Adds either prerequisites or whatever is in CrawlURI outlinks to the
 * Frontier.  Run a Scoper ahead of this processor so only links that
 * are in-scope get scheduled.
 *
 * @author stack
 */
public class FrontierScheduler extends Processor
        implements FetchStatusCodes {

    private static final long serialVersionUID = -5178775477602250542L;

    private static Logger LOGGER =
            Logger.getLogger(FrontierScheduler.class.getName());

    //URL简单去重
    private static final HashSet<String> readlyURLs = new HashSet<>();

    //该页面会重复下载几万次，故在去重阶段进行判断
    private static boolean isListExist = false;
    private static final String listUrlPrex = "http://cs.whu.edu.cn/plus/list.php";


    /**
     * @param name Name of this filter.
     */
    public FrontierScheduler(String name) {
        super(name, "FrontierScheduler. 'Schedule' with the Frontier " +
                "any CandidateURIs carried by the passed CrawlURI. " +
                "Run a Scoper before this " +
                "processor so links that are not in-scope get bumped from the " +
                "list of links (And so those in scope get promoted from Link " +
                "to CandidateURI).");
    }

    protected void innerProcess(final CrawlURI curi) {
        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest(getName() + " processing " + curi);
        }

        // Handle any prerequisites when S_DEFERRED for prereqs
        if (curi.hasPrerequisiteUri() && curi.getFetchStatus() == S_DEFERRED) {
            handlePrerequisites(curi);
            return;
        }
        synchronized (this) {
            if (StartHeritrix.doneSeeds.contains(curi.getSeedSource())) {
            } else try {
                if (curi.getLevel() > SysConfig.getDepth()) {

                    //该种子站点已经完成爬取
                    StartHeritrix.doneSeeds.add(curi.getSeedSource());
                    try {
                        SeedsService.markSeedDone(curi.getSeedSource());
                    } catch (SQLException e) {

                        LOGGER.info(getName() + "标志为完成时错误" + curi);
                    }
                } else {
                    for (CandidateURI cauri : curi.getOutCandidates()) {

                        String nowUrl = Toolkit.trimSlash(cauri.toString());
                        if (ExtractorHTML.isRejectSuffix(nowUrl)) {
                            LOGGER.info(cauri + "后缀名未通过");
                            continue;
                        }

                        if (!isCommonDomain(nowUrl, Toolkit.trimSlash(curi.getSeedSource().toString()))) {
                            //检测是否是同一Domain下
                            LOGGER.info(cauri + "非相同Domain");
                        }

                        if (readlyURLs.contains(nowUrl))
                            continue;
                        try {
                            if (isUrlExist(nowUrl)) {
                                readlyURLs.add(cauri.toString());
                                System.out.println("url已经存在");
                                continue;
                            }

                            if (nowUrl.startsWith(listUrlPrex)) {
                                if (isListExist) {
                                    continue;
                                } else if (countLikeUrl(listUrlPrex + "%") >= 2) {
                                    //TODO 可优化为Trie树
                                    isListExist = true;
                                    continue;
                                }
                            }

                        } catch (SQLException e) {
                        }

                        String source = curi.getSeedSource();
                        cauri.setSeedSource(source);
                        cauri.setParent(nowUrl);
                        schedule(cauri);
                    }
                    readlyURLs.add(curi.toString());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void handlePrerequisites(CrawlURI curi) {
        schedule((CandidateURI) curi.getPrerequisiteUri());
    }

    /**
     * Schedule the given {@li nk CandidateURI CandidateURI} with the Frontier.
     *
     * @param caUri The CandidateURI to be scheduled.
     */
    protected void schedule(CandidateURI caUri) {
        getController().getFrontier().schedule(caUri);
    }

    private boolean isCommonDomain(String sourceUrl, String url) {
        return Toolkit.getDomainForUrl(sourceUrl).equals(Toolkit.getDomainForUrl(url));
    }
}
