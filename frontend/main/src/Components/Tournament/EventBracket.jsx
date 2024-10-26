import { SingleEliminationBracket, Match, SVGViewer, MATCH_STATES, createTheme } from '@g-loot/react-tournament-brackets';
import React from 'react';


export default function EventBracket({ matches, height, width }) {
    console.log("Height: ", height + " Width: ", width);
    return (
        <SingleEliminationBracket
            theme={GlootTheme}
            matches={matches}
            matchComponent={Match}
            svgWrapper={({ children, ...props }) => (
                <SVGViewer
                    width={width}
                    height={height}
                    background={GlootTheme.svgBackground}
                    SVGBackground={GlootTheme.svgBackground}
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

const GlootTheme = createTheme({
    textColor: { main: '#000000', highlighted: '#F4F2FE', dark: '#707582' },
    matchBackground: { wonColor: '#2D2D59', lostColor: '#1B1D2D' },
    score: {
        background: {
            wonColor: `#10131C`,
            lostColor: '#10131C',
        },
        text: { highlightedWonColor: '#7BF59D', highlightedLostColor: '#FB7E94' },
    },
    border: {
        color: '#292B43',
        highlightedColor: 'RGBA(152,82,242,0.4)',
    },
    roundHeader: { backgroundColor: '#3B3F73', fontColor: '#F4F2FE' },
    connectorColor: '#3B3F73',
    connectorColorHighlight: 'RGBA(152,82,242,0.4)',
    svgBackground: '#0F121C',
});

