# 🚀 Guía Maestra de Despliegue SIM-Pay

Para cumplir con el requerimiento de usar Vercel y evitar Railway, hemos dividido el despliegue en dos partes optimizadas:

1.  **Backend (Java Spring Boot):** Desplegado en **Render** (Alternativa a Railway).
    *   👉 Ver guía: [DEPLOY-RENDER.md](./DEPLOY-RENDER.md)

2.  **Frontend (Next.js):** Desplegado en **Vercel**.
    *   👉 Ver guía: [DEPLOY-VERCEL-FRONTEND.md](./DEPLOY-VERCEL-FRONTEND.md)

---

## 💡 ¿Por qué esta arquitectura?
*   **Vercel** es excelente para Next.js pero no soporta aplicaciones Java de larga duración.
*   **Render** ofrece un entorno robusto para Docker/Java gratuito (similar a Railway).
*   Esta combinación te da lo mejor de ambos mundos sin costo inicial.