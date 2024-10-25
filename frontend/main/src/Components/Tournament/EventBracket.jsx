import { SingleEliminationBracket, Match, SVGViewer, MATCH_STATES, createTheme } from '@g-loot/react-tournament-brackets';
import React from 'react';


export default function EventBracket({ matches, height, width }) {
    console.log("Height: ", height + " Width: ", width);
    return (
        <SingleEliminationBracket
            theme={WhiteTheme}
            matches={matches}
            matchComponent={Match}
            svgWrapper={({ children, ...props }) => (
                <SVGViewer
                    width={width}
                    height={height}
                    background={WhiteTheme.svgBackground}
                    SVGBackground={WhiteTheme.svgBackground}
                    {...props}
                >
                    {children}
                </SVGViewer>
            )}
            onMatchClick={(match) => console.log(match)}
            onPartyClick={(match) => console.log(match)}
        />
    );

}

const WhiteTheme = createTheme({
    textColor: { main: '#000000', highlighted: '#07090D', dark: '#3E414D' },
    matchBackground: { wonColor: '#daebf9', lostColor: '#96c6da' },
    score: {
      background: { wonColor: '#87b2c4', lostColor: '#87b2c4' },
      text: { highlightedWonColor: '#7BF59D', highlightedLostColor: '#FB7E94' },
    },
    border: {
      color: '#CED1F2',
      highlightedColor: '#da96c6',
    },
    roundHeader: { backgroundColor: '#da96c6', fontColor: '#000' },
    connectorColor: '#CED1F2',
    connectorColorHighlight: '#da96c6',
    svgBackground: '#FAFAFA',
  });