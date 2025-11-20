// Phone Interview Page (3rd Stage Learning)
// Real-time emotion analysis using webcam

import React, { useRef, useState, useEffect } from 'react';
import { interviewService, EmotionAnalysisResponse } from '../services/interviewService';

interface PhoneInterviewPageProps {
  userId: string;
  onComplete: () => void;
  onBack?: () => void;
}

export const PhoneInterviewPage: React.FC<PhoneInterviewPageProps> = ({
  userId,
  onComplete,
  onBack
}) => {
  const videoRef = useRef<HTMLVideoElement>(null);
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const [sessionId, setSessionId] = useState<number | null>(null);
  const [isWebcamActive, setIsWebcamActive] = useState(false);
  const [currentEmotion, setCurrentEmotion] = useState<EmotionAnalysisResponse | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [isAnalyzing, setIsAnalyzing] = useState(false);
  const streamRef = useRef<MediaStream | null>(null);
  const analyzeIntervalRef = useRef<number | null>(null);

  // Start webcam and session
  const startInterview = async () => {
    try {
      setError(null);

      // Request webcam access
      const stream = await navigator.mediaDevices.getUserMedia({
        video: {
          width: { ideal: 640 },
          height: { ideal: 480 },
          facingMode: 'user'
        },
        audio: false
      });

      streamRef.current = stream;

      if (videoRef.current) {
        videoRef.current.srcObject = stream;
        videoRef.current.play();
      }

      setIsWebcamActive(true);

      // Start interview session
      const response = await interviewService.startSession({
        userId,
        stage: 3
      });

      setSessionId(response.sessionId);

      // Start emotion analysis every 2 seconds
      startEmotionAnalysis(response.sessionId);

    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to start interview');
      console.error('Error starting interview:', err);
    }
  };

  // Capture frame and send for analysis
  const captureAndAnalyze = async (sessionId: number) => {
    if (!videoRef.current || !canvasRef.current) return;

    const canvas = canvasRef.current;
    const video = videoRef.current;

    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;

    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    // Draw current frame to canvas
    ctx.drawImage(video, 0, 0);

    // Convert to base64
    const imageData = canvas.toDataURL('image/jpeg', 0.7);

    try {
      setIsAnalyzing(true);

      const result = await interviewService.analyzeEmotion({
        sessionId,
        imageData,
        timestamp: Date.now()
      });

      setCurrentEmotion(result);

    } catch (err) {
      console.error('Error analyzing emotion:', err);
    } finally {
      setIsAnalyzing(false);
    }
  };

  // Start periodic emotion analysis
  const startEmotionAnalysis = (sessionId: number) => {
    // Initial analysis
    setTimeout(() => captureAndAnalyze(sessionId), 1000);

    // Periodic analysis every 2 seconds
    analyzeIntervalRef.current = window.setInterval(() => {
      captureAndAnalyze(sessionId);
    }, 2000);
  };

  // Stop emotion analysis
  const stopEmotionAnalysis = () => {
    if (analyzeIntervalRef.current) {
      clearInterval(analyzeIntervalRef.current);
      analyzeIntervalRef.current = null;
    }
  };

  // End interview
  const endInterview = async () => {
    try {
      stopEmotionAnalysis();

      if (sessionId) {
        await interviewService.endSession(sessionId);
      }

      // Stop webcam
      if (streamRef.current) {
        streamRef.current.getTracks().forEach(track => track.stop());
        streamRef.current = null;
      }

      setIsWebcamActive(false);
      onComplete();

    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to end interview');
      console.error('Error ending interview:', err);
    }
  };

  // Cleanup on unmount
  useEffect(() => {
    return () => {
      stopEmotionAnalysis();
      if (streamRef.current) {
        streamRef.current.getTracks().forEach(track => track.stop());
      }
    };
  }, []);

  // Get emotion color
  const getEmotionColor = (emotion: string) => {
    const colors: Record<string, string> = {
      happy: '#8CC63F',
      neutral: '#999',
      sad: '#4A90E2',
      angry: '#FF6B6B',
      surprised: '#FFA500',
      fear: '#9B59B6',
      disgust: '#E67E22'
    };
    return colors[emotion.toLowerCase()] || '#999';
  };

  return (
    <div style={{
      width: '393px',
      height: '852px',
      backgroundColor: '#F5F9F0',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      position: 'relative',
      overflow: 'hidden',
      padding: '20px',
    }}>
      {/* Header */}
      <div style={{
        width: '100%',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '20px',
      }}>
        <img
          src="/img/Abocado_Logo.png"
          alt="Logo"
          style={{ width: '80px', height: '58px' }}
        />
        {onBack && (
          <button
            onClick={onBack}
            style={{
              background: 'none',
              border: 'none',
              fontSize: '24px',
              cursor: 'pointer',
              color: '#666',
            }}
          >
            ←
          </button>
        )}
      </div>

      {/* Title */}
      <h1 style={{
        fontFamily: 'Fredoka, Inter, sans-serif',
        fontWeight: 600,
        fontSize: '28px',
        color: '#000',
        marginBottom: '10px',
        textAlign: 'center',
      }}>
        Phone Interview
      </h1>

      <p style={{
        fontFamily: 'Inter, sans-serif',
        fontSize: '14px',
        color: '#666',
        marginBottom: '20px',
        textAlign: 'center',
      }}>
        3rd Stage Learning - Emotion Analysis
      </p>

      {/* Error Message */}
      {error && (
        <div style={{
          width: '100%',
          padding: '15px',
          backgroundColor: '#FFE6E6',
          borderRadius: '10px',
          marginBottom: '20px',
          color: '#FF6B6B',
          fontSize: '14px',
          textAlign: 'center',
        }}>
          {error}
        </div>
      )}

      {/* Video Container */}
      <div style={{
        width: '100%',
        maxWidth: '350px',
        height: '260px',
        backgroundColor: '#000',
        borderRadius: '15px',
        overflow: 'hidden',
        marginBottom: '20px',
        position: 'relative',
      }}>
        <video
          ref={videoRef}
          style={{
            width: '100%',
            height: '100%',
            objectFit: 'cover',
          }}
          playsInline
          muted
        />
        <canvas ref={canvasRef} style={{ display: 'none' }} />

        {/* Analyzing Indicator */}
        {isAnalyzing && (
          <div style={{
            position: 'absolute',
            top: '10px',
            right: '10px',
            backgroundColor: '#8CC63F',
            color: '#FFF',
            padding: '5px 10px',
            borderRadius: '5px',
            fontSize: '12px',
            fontWeight: 600,
          }}>
            Analyzing...
          </div>
        )}
      </div>

      {/* Emotion Display */}
      {currentEmotion && (
        <div style={{
          width: '100%',
          maxWidth: '350px',
          backgroundColor: '#FFF',
          borderRadius: '15px',
          padding: '20px',
          marginBottom: '20px',
          boxShadow: '0px 4px 8px rgba(0, 0, 0, 0.1)',
        }}>
          <div style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            marginBottom: '15px',
          }}>
            <span style={{
              fontFamily: 'Fredoka, Inter, sans-serif',
              fontSize: '20px',
              fontWeight: 600,
              color: getEmotionColor(currentEmotion.dominantEmotion),
            }}>
              {currentEmotion.dominantEmotion.toUpperCase()}
            </span>
            <span style={{
              fontFamily: 'Inter, sans-serif',
              fontSize: '16px',
              color: '#666',
            }}>
              {currentEmotion.confidence.toFixed(1)}%
            </span>
          </div>

          {/* All Emotions */}
          <div style={{ fontSize: '12px', color: '#999' }}>
            {Object.entries(currentEmotion.emotions)
              .sort((a, b) => b[1] - a[1])
              .slice(0, 3)
              .map(([emotion, value]) => (
                <div
                  key={emotion}
                  style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    marginBottom: '5px',
                  }}
                >
                  <span>{emotion}</span>
                  <span>{value.toFixed(1)}%</span>
                </div>
              ))}
          </div>
        </div>
      )}

      {/* Control Buttons */}
      <div style={{
        position: 'absolute',
        bottom: '40px',
        width: '100%',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        gap: '15px',
        padding: '0 20px',
      }}>
        {!isWebcamActive ? (
          <button
            onClick={startInterview}
            style={{
              width: '100%',
              maxWidth: '300px',
              height: '60px',
              backgroundColor: '#8CC63F',
              border: 'none',
              borderRadius: '10px',
              cursor: 'pointer',
              boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
              fontFamily: 'Fredoka, Inter, sans-serif',
              fontWeight: 600,
              fontSize: '20px',
              color: '#FFFFFF',
            }}
          >
            📞 Start Phone Call
          </button>
        ) : (
          <button
            onClick={endInterview}
            style={{
              width: '100%',
              maxWidth: '300px',
              height: '60px',
              backgroundColor: '#FF6B6B',
              border: 'none',
              borderRadius: '10px',
              cursor: 'pointer',
              boxShadow: '0px 4px 4px rgba(0, 0, 0, 0.25)',
              fontFamily: 'Fredoka, Inter, sans-serif',
              fontWeight: 600,
              fontSize: '20px',
              color: '#FFFFFF',
            }}
          >
            End Call
          </button>
        )}
      </div>
    </div>
  );
};

export default PhoneInterviewPage;
