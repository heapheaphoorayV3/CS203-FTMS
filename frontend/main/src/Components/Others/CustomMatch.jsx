import React from 'react';
import {
  Score,
  Side,
  StyledMatch,
  Team,
  TopText,
  BottomText,
  Wrapper,
  Line,
  Anchor,
} from './CustomMatchStyles';


function CustomMatch({
  bottomHovered,
  bottomParty,
  bottomText,
  bottomWon,
  match,
  onMatchClick,
  onMouseEnter,
  onMouseLeave,
  onPartyClick,
  topHovered,
  topParty,
  topText,
  topWon,
}) {
  return (
    <Wrapper>
      <StyledMatch>
        <Side
          onMouseEnter={() => onMouseEnter(topParty.id)}
          onMouseLeave={onMouseLeave}
          won={topWon}
          hovered={topHovered}
          onClick={() => onPartyClick?.(topParty, topWon)}
        >
          <Team>{topParty?.name}</Team>
          <Score won={topWon}>{topParty?.resultText}</Score>
        </Side>
        <Line highlighted={topHovered || bottomHovered} />
        <Side
          onMouseEnter={() => onMouseEnter(bottomParty.id)}
          onMouseLeave={onMouseLeave}
          won={bottomWon}
          hovered={bottomHovered}
          onClick={() => onPartyClick?.(bottomParty, bottomWon)}
        >
          <Team>{bottomParty?.name}</Team>
          <Score won={bottomWon}>{bottomParty?.resultText}</Score>
        </Side>
      </StyledMatch>
      <BottomText>{bottomText ?? ' '}</BottomText>
    </Wrapper>
  );
}

export default CustomMatch;