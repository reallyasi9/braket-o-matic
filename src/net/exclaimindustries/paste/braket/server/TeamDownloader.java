/**
 * This file is part of braket-o-matic.
 *
 * braket-o-matic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * braket-o-matic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with braket-o-matic.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.exclaimindustries.paste.braket.server;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.exclaimindustries.paste.braket.client.BraketTeam;
import net.exclaimindustries.paste.braket.client.RGBAColor;
import net.exclaimindustries.paste.braket.client.TeamName;

import org.jdom2.CDATA;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

/**
 * A class to download and parse the ESPN feed to discover all the teams.
 * 
 * @author paste
 *         http://feeds.espn.go.com/ncb/widgets/s?c=teams&a=feed&team_id=543
 */
public class TeamDownloader {

    /**
     * Just tag the team ID at the end!
     */
    private static String feedString =
            "http://feeds.espn.go.com/ncb/widgets/s?c=teams&a=feed&team_id=";

    /**
     * Download and parse the team information from the ESPN feed, one team at a
     * time.
     * 
     * @param tournament
     *            The tournament to which the teams should be referenced.
     * @param teamIds
     *            The Ids of the teams to download (valid numbers are 1 to about
     *            545 or so)
     * @return A list of parsed teams from the ESPN feed, with the endTeamId
     *         excluded.
     * @throws IOException
     * @throws JDOMException
     */
    public static Collection<BraketTeam> downloadTeams(Iterable<Long> teamIds)
            throws IOException {
        LinkedList<BraketTeam> teamList = new LinkedList<BraketTeam>();

        for (Long iTeam : teamIds) {
            URI uri = URI.create(feedString + iTeam.toString());
            SAXBuilder feedParser = new SAXBuilder();
            Document feedDocument;
            try {
                feedDocument = feedParser.build(uri.toURL());
            } catch (JDOMException e) {
                throw new IOException(e);
            }

            BraketTeam team = parseTeam(feedDocument);

            if (team == null) {
                // Couldn't find any info about the team, so skip it.
                continue;
            }

            // Make sure that the given ID matches the ID in the team (else this
            // is not a real team)
            if (!team.getId().equals(iTeam)) {
                continue;
            }

            teamList.add(team);
        }

        return teamList;
    }

    public static BraketTeam parseTeam(Document document)
            throws MalformedURLException {
        // Parse out the details
        Element rootNode = document.getRootElement();

        // I need the espn namespace
        List<Namespace> namespaceList = rootNode.getNamespacesInScope();
        Namespace ns = Namespace.NO_NAMESPACE;
        for (Namespace namespace : namespaceList) {
            if (namespace.getPrefix() == "espn") {
                ns = namespace;
                break;
            }
        }

        // Find the "item" element that has the right stuff in it
        Element channel = rootNode.getChild("channel");
        List<Element> items = channel.getChildren("item");
        Element teamElement = null;
        for (Element item : items) {
            if (item.getChild("teamAbbrev", ns) != null) {
                teamElement = item.clone();
                break;
            }
        }

        if (teamElement == null) {
            // Couldn't find any info about the team, so skip it.
            return null;
        }

        // Make sure that the given ID matches the ID in the team (else this
        // is not a real team)
        Long teamId = Long.valueOf(teamElement.getChildText("teamId", ns));

        BraketTeam team = new BraketTeam();
        team.setId(teamId);

        String abbreviation = digOutCDATA(teamElement, "teamAbbrev", ns);
        String displayName = digOutCDATA(teamElement, "teamDisplayName", ns);
        String location = digOutCDATA(teamElement, "teamLocation", ns);
        String nickname = digOutCDATA(teamElement, "teamNickname", ns);
        String teamNameString = teamElement.getChildText("teamName", ns);
        String teamColorString = "#" + teamElement.getChildText("teamColor", ns);
        String teamLogoUrl = teamElement.getChildText("teamLogo", ns);

        TeamName teamName =
                new TeamName(location, teamNameString, displayName, nickname,
                        abbreviation);
        team.setName(teamName);

        try {
            if (teamColorString != null) {
                RGBAColor color = RGBAColor.fromCSSString(teamColorString);
                team.setColor(color);
            }
        } catch (Exception e) {
            // TODO Is this okay?
        }

        // Save the image name (should be the same as the downloaded version)
        URL url = new URL(teamLogoUrl);
        File file = new File(url.getPath());
        String teamLogoName = file.getName();
        team.setPicture(teamLogoName);

        return team;
    }

    private static String digOutCDATA(Element element, String cname,
            Namespace ns) {
        List<Content> list = element.getChild(cname, ns).getContent();
        String returnString = null;
        for (Content content : list) {
            if (content instanceof CDATA) {
                returnString = ((CDATA) content).getValue();
                break;
            }
        }
        return returnString;
    }
}
