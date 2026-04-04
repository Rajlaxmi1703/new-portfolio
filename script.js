/* =============================================
   RAJLAXMI REDE — PORTFOLIO  script.js
   =============================================*/

/* ── MODULE DATA ── */
const modulesData = [
  {
    num:'01', icon:'🌐', title:'Web Development',
    desc:'Build responsive, modern websites using HTML5, CSS3, JavaScript, and cutting-edge frameworks.',
    link:'web-development.html'
  },
   {
  num:'02', icon:'🎨', title:'UI/UX Design',
  desc:'Design beautiful and user-friendly interfaces using Figma. Creating wireframes, prototypes and interactive designs.',
  link:'ui-ux-design.html'
},
  {
    num:'03', icon:'✏️', title:'CAD Design',
    desc:'Master 2D/3D modeling with professional computer-aided design software and parametric modeling.',
    link:'cad-design.html'
  },
  {
    num:'04', icon:'🖨️', title:'3D Printing',
    desc:'Learn additive manufacturing techniques, materials science, and rapid prototyping methods.',
    link:'3d-printing.html'
  },
  {
    num:'05', icon:'🔬', title:'Laser Cutting',
    desc:'Master precision cutting and engraving with laser technology for various materials.',
    link:'lasercutting.html'
  },
  
  {
    num:'06', icon:'🔌', title:'PCB Design',
    desc:'Design professional printed circuit boards for custom electronics projects.',
    link:'pcb-design.html'
  },
  {
    num:'07', icon:'📡', title:'Internet of Things',
    desc:'Connect devices to the cloud and build smart, interconnected systems.',
    link:'iot.html'
  },
  {
    num:'08', icon:'☕', title:'Java Programming',
    desc:'Master object-oriented programming with Java and build robust applications.',
    link:'java-programming.html'
  },
  {
    num:'09', icon:'🌐', title:'Networking',
    desc:'Understand computer networks, protocols, and modern communication systems.',
    link:'networking.html'
  },
  {
    num:'10', icon:'🤖', title:'AI / ML',
    desc:'Dive into artificial intelligence and machine learning fundamentals and real applications.',
    link:'ai-ml.html'
  }
];

/* ── RENDER MODULE CARDS ── */
function renderModules() {
  const grid = document.getElementById('modulesGrid');
  if (!grid) return;

  modulesData.forEach((mod, i) => {
    const delay = i % 3 === 1 ? ' d1' : i % 3 === 2 ? ' d2' : '';
    const card  = document.createElement('div');
    card.className = `module-card fade-up${delay}`;
    card.innerHTML = `
      <div class="mod-icon">${mod.icon}</div>
      <p class="mod-num">Module ${mod.num}</p>
      <h3 class="mod-title">${mod.title}</h3>
      <p class="mod-desc">${mod.desc}</p>
      <a href="${mod.link}" class="mod-link">
        Explore Module <i class="fas fa-arrow-right"></i>
      </a>
    `;
    grid.appendChild(card);
  });

  document.querySelectorAll('.module-card.fade-up')
    .forEach(el => revealObserver.observe(el));
}

/* ── NAVBAR SCROLL ── */
const navbar = document.getElementById('navbar');

function onScroll() {
  navbar.classList.toggle('scrolled', window.scrollY > 20);
  updateActiveNav();
  toggleBackToTop();
}

/* ── ACTIVE NAV LINK ── */
const sections = document.querySelectorAll('section[id]');
const navLinks  = document.querySelectorAll('.nav-link');

function updateActiveNav() {
  let current = '';
  sections.forEach(s => {
    if (window.scrollY >= s.offsetTop - 110) current = s.id;
  });
  navLinks.forEach(a =>
    a.classList.toggle('active', a.getAttribute('href') === `#${current}`)
  );
}

/* ── MOBILE NAV ── */
const ham    = document.getElementById('hamburger');
const mobNav = document.getElementById('mobNav');

function closeMob() {
  ham.classList.remove('open');
  mobNav.classList.remove('open');
}
window.closeMob = closeMob;

ham.addEventListener('click', () => {
  ham.classList.toggle('open');
  mobNav.classList.toggle('open');
});
navLinks.forEach(l => l.addEventListener('click', closeMob));
document.addEventListener('click', e => {
  if (!mobNav.contains(e.target) && !ham.contains(e.target)) closeMob();
});

/* ── BACK TO TOP ── */
const bttBtn = document.getElementById('backToTop');

function toggleBackToTop() {
  bttBtn.classList.toggle('visible', window.scrollY > 400);
}
bttBtn.addEventListener('click', () =>
  window.scrollTo({ top: 0, behavior: 'smooth' })
);

/* ── SCROLL REVEAL ── */
const revealObserver = new IntersectionObserver(entries => {
  entries.forEach(e => {
    if (e.isIntersecting) {
      e.target.classList.add('visible');
      revealObserver.unobserve(e.target);
    }
  });
}, { threshold: 0.05 });

function initReveal() {
  document.querySelectorAll('.fade-up')
    .forEach(el => revealObserver.observe(el));
}

/* ── CONTACT FORM ── */
function handleSubmit(e) {
  e.preventDefault();
  const btn = document.getElementById('submitBtn');
  btn.disabled = true;
  btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Sending…';
  setTimeout(() => {
    btn.innerHTML        = '<i class="fas fa-check"></i> Message Sent!';
    btn.style.background = '#059669';
    btn.style.borderColor= '#059669';
    e.target.reset();
    setTimeout(() => {
      btn.disabled         = false;
      btn.innerHTML        = '<i class="fas fa-paper-plane"></i> Send Message';
      btn.style.background = '';
      btn.style.borderColor= '';
    }, 3500);
  }, 1200);
}
window.handleSubmit = handleSubmit;

/* ── INIT ── */
window.addEventListener('scroll', onScroll, { passive: true });

document.addEventListener('DOMContentLoaded', () => {
  renderModules();
  initReveal();
  updateActiveNav();
  onScroll();
});
// ```

// ---

// ## 🌐 web-development.html

// This is a long file — download it from the file shared above ⬆️ (the `web-development` file I already sent you).

// ---

// ## ✅ Steps
// ```
// 1. Open script.js      → Select All → Delete → Paste new code → Save
// 2. Open index.html     → Select All → Delete → Paste new code → Save
// 3. web-development.html → already downloaded above
// 4. style.css           → DO NOT TOUCH ✅
// 5. Open index.html in Chrome → Ctrl+Shift+R
