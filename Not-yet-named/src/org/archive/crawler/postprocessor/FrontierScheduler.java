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


import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.archive.crawler.StartHeritrix;
import org.archive.crawler.datamodel.CandidateURI;
import org.archive.crawler.datamodel.CrawlURI;
import org.archive.crawler.datamodel.FetchStatusCodes;
import org.archive.crawler.db.DataService;
import org.archive.crawler.db.SeedsService;
import org.archive.crawler.framework.Processor;

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

        try {
            if (DataService.isUrlExist(curi.toString())) {
                System.out.println("url已经存在");
                return;
            }
        } catch (SQLException e) {
        }

        synchronized (this) {
            if (StartHeritrix.doneSeeds.contains(curi.getSeedSource())) {
            } else if (curi.getLevel() > 10) {

                //该种子站点已经完成爬取
                StartHeritrix.doneSeeds.add(curi.getSeedSource());
                try {
                    SeedsService.markSeedDone(curi.getSeedSource());
                } catch (SQLException e) {
                    //e.printStackTrace();
                    LOGGER.info(getName() + "标志为完成时错误" + curi);
                }
            } else {
                for (CandidateURI cauri : curi.getOutCandidates()) {
                    String source = curi.getSeedSource();
                    cauri.setSeedSource(source);
                    schedule(cauri);
                }
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
}
