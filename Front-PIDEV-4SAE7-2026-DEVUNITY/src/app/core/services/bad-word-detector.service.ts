import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class BadWordDetectorService {
  
  // Comprehensive list of inappropriate words (without regex special chars)
  private badWords = [
    // Profanity
    'damn', 'hell', 'crap', 'piss', 'ass', 'bastard', 'bitch', 'dick', 'cock',
    // Racial/ethnic slurs
    'nigga', 'nigger', 'faggot',
    // Sexual
    'fuck', 'sex', 'porn', 'xxx',
    // Extreme profanity
    'shit', 'asshole', 'motherfucker',
    // Derogatory terms
    'retard', 'gay', 'homo', 'slut', 'whore',
    // Drug-related
    'cocaine', 'meth', 'heroin', 'weed', 'pot', 'marijuana',
    // Violence/threats
    'kill', 'murder', 'rape', 'bomb', 'terrorist',
  ];

  constructor() { }

  /**
   * Escape special regex characters
   */
  private escapeRegExp(text: string): string {
    return text.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
  }

  /**
   * Check if text contains bad words
   * @param text - Text to check
   * @returns Object with found flag and list of detected bad words
   */
  checkText(text: string): { hasBadWords: boolean; badWordsList: string[] } {
    if (!text || typeof text !== 'string') {
      return { hasBadWords: false, badWordsList: [] };
    }

    const lowerText = text.toLowerCase();
    const detectedWords = new Set<string>();

    // Check each bad word
    this.badWords.forEach(badWord => {
      try {
        // Escape the bad word for use in regex
        const escapedWord = this.escapeRegExp(badWord);
        
        // Create regex patterns to match:
        // 1. Word with word boundaries
        // 2. Word with leetspeak variations
        const patterns = [
          new RegExp(`\\b${escapedWord}\\b`, 'gi'), // Exact word match with boundaries
          new RegExp(escapedWord.replace(/a/g, '[a@4]')
                                .replace(/e/g, '[e3]')
                                .replace(/i/g, '[i1!]')
                                .replace(/o/g, '[o0]')
                                .replace(/s/g, '[s$5]')
                                .replace(/t/g, '[t7]'), 'gi') // Leetspeak variations
        ];

        patterns.forEach(pattern => {
          const matches = lowerText.match(pattern);
          if (matches) {
            matches.forEach(match => {
              detectedWords.add(match.trim());
            });
          }
        });
      } catch (error) {
        console.warn(`Error processing bad word: ${badWord}`, error);
      }
    });

    return {
      hasBadWords: detectedWords.size > 0,
      badWordsList: Array.from(detectedWords)
    };
  }

  /**
   * Get warning message based on detected words
   */
  getWarningMessage(badWordsList: string[]): string {
    if (badWordsList.length === 0) {
      return '';
    }

    const wordList = badWordsList.slice(0, 3).join(', ');
    const moreText = badWordsList.length > 3 ? ` and ${badWordsList.length - 3} more` : '';
    
    return `⚠️ Inappropriate language detected: ${wordList}${moreText}. Please revise your response.`;
  }

  /**
   * Replace bad words with asterisks
   */
  censorText(text: string): string {
    let censoredText = text;

    this.badWords.forEach(badWord => {
      try {
        const escapedWord = this.escapeRegExp(badWord);
        const pattern = new RegExp(`\\b${escapedWord}\\b`, 'gi');
        censoredText = censoredText.replace(pattern, '*'.repeat(badWord.length));
      } catch (error) {
        console.warn(`Error censoring bad word: ${badWord}`, error);
      }
    });

    return censoredText;
  }
}
