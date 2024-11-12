import {
  SingleEliminationBracket,
  Match,
  SVGViewer,
  createTheme,
} from "@g-loot/react-tournament-brackets";
// import { GlootTheme, WhiteTheme } from '../Others/EventBracketTheme';
import CustomMatch from "../Others/CustomMatch";
import React from "react";

export default function EventBracket({ matches, height, width }) {
  // console.log("Height: ", height + " Width: ", width);
  return (
    <SingleEliminationBracket
      theme={WhiteTheme}
      matches={matches}
      matchComponent={CustomMatch}
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
    />
  );
}

export const WhiteTheme = createTheme({
  textColor: { main: "#000000", highlighted: "#07090D", dark: "#3E414D" },
  matchBackground: { wonColor: "#cfdee7", lostColor: "#cfdee7" },
  score: {
    background: { wonColor: "#f37748", lostColor: "#00a7e1" },
    text: { highlightedWonColor: "#FFFFFF", highlightedLostColor: "#FFFFFF" },
  },
  border: {
    color: "#edf2fb",
    highlightedColor: "#f37748",
  },
  roundHeader: { backgroundColor: "#4472ca", fontColor: "#FFFFFF" },
  connectorColor: '#CED1F2',
  connectorColorHighlight: '#f37748',
  svgBackground: "#FFFFFF",
});
