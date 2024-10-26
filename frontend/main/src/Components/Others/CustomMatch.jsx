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
} from './styles';

export type MatchComponentProps = {
    match: Match;
  
    onMatchClick: (args: {
      match: Match;
      topWon: boolean;
      bottomWon: boolean;
      event: React.MouseEvent<HTMLAnchorElement, MouseEvent>;
    }) => void;
  
    onPartyClick: (party: Participant, partyWon: boolean) => void;
  
    onMouseEnter: (partyId: string | number) => void;
  
    onMouseLeave: () => void;
  
    topParty: Participant;
  
    bottomParty: Participant;
  
    topWon: boolean;
  
    bottomWon: boolean;
  
    topHovered: boolean;
  
    bottomHovered: boolean;
  
    topText: string;
  
    bottomText: string;
  
    connectorColor?: string;
  
    computedStyles?: ComputedOptions;
  
    teamNameFallback: string;
  
    resultFallback: (participant: Participant) => string;
  };

function Match({
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
}: MatchComponentProps) {
  return (
    <Wrapper>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <TopText>{topText}</TopText>
        {(match.href || typeof onMatchClick === 'function') && (
          <Anchor
            href={match.href}
            onClick={event =>
              onMatchClick?.({ match, topWon, bottomWon, event })
            }
          >
            <TopText>Match Details</TopText>
          </Anchor>
        )}
      </div>
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